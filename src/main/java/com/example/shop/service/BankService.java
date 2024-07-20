package com.example.shop.service;

import com.example.shop.dto.ExchangeRateDTO;
import com.example.shop.dto.ResponseDTO;
import com.example.shop.model.ExchangeRateDummy;
import com.example.shop.repository.ExchangeRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BankService {
    @Autowired
    ExchangeRateRepository exchangeRateRepository;
    @Autowired
    private RestTemplate restTemplate=new RestTemplate();

    public ResponseDTO fetchCurrency() {
        LocalDate localDate = LocalDate.now().minusDays(1);
        ExchangeRateDummy exchangeRate=fetchExchangeForDate(localDate);
        System.out.println(exchangeRate.toString());
        List<ExchangeRateDTO> exchangeRateDummies=new ArrayList<>();
        exchangeRateDummies.add(new ExchangeRateDTO(exchangeRate));
        return new ResponseDTO(exchangeRateDummies);
    }
    public ResponseDTO fetchTargetCurrency(String targetCurrency, Integer noOfDays) {

        LocalDate localDate = LocalDate.now().minusDays(1);
        final List<ExchangeRateDTO> exchangeRateList = new ArrayList<>();

        for (int i = 0; i < noOfDays; i++) {
            ExchangeRateDummy exchangeRate = fetchExchangeForDate(localDate.minusDays(i));
            Map<String, Double> filteredRates = exchangeRate.getRates().entrySet().stream()
                    .filter(entry -> Arrays.asList(targetCurrency).contains(entry.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            ExchangeRateDTO exchangeRateDTO=new ExchangeRateDTO(exchangeRate);
            exchangeRateDTO.setRates(filteredRates);
            exchangeRateList.add(exchangeRateDTO);
        }
        return new ResponseDTO(exchangeRateList);
    }
    private ExchangeRateDummy fetchExchangeForDate(LocalDate localDate){
        ExchangeRateDummy exchangeRateEntity = fetchExchangeRateForDateFromDB(localDate);
        if (exchangeRateEntity == null) {
            exchangeRateEntity=this.fetchAndSaveExchangeRate(localDate);
        }

        return exchangeRateEntity;
    }
    public ExchangeRateDummy fetchAndSaveExchangeRate(LocalDate localDate) {
        String url = "https://api.frankfurter.app/"+localDate.toString()+"?from=USD"; // Replace with actual API URL
        ExchangeRateDummy exchangeRate = restTemplate.getForObject(url, ExchangeRateDummy.class);
        Map<String, Double> filteredRates = exchangeRate.getRates().entrySet().stream()
                .filter(entry -> Arrays.asList("USD", "GBP","EUR","JPY","CZK").contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        exchangeRate.setRates(filteredRates);
        System.out.println("saving results to DB "+exchangeRate.toString());
        exchangeRateRepository.save(exchangeRate);
        return exchangeRate;
    }

    private ExchangeRateDummy fetchExchangeRateForDateFromDB(LocalDate localDate) {
        final List<ExchangeRateDummy> optionalExchangeRateEntity = exchangeRateRepository.findByDate(localDate);
        if(optionalExchangeRateEntity.size()!=0){
            System.out.println("Exchange rates avaialable for"+localDate.toString());
            return optionalExchangeRateEntity.get(0);
        }
        else {
            System.out.println("No exchange rates are avaialable for"+localDate.toString());
            return null;
        }
    }

}
