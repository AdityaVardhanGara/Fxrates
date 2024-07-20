package com.example.shop.service;

import com.example.shop.dto.ExchangeRateDTO;
import com.example.shop.dto.ResponseDTO;
import com.example.shop.model.ExchangeRateDummy;
import com.example.shop.repository.ExchangeRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class BankServiceTest {

    @Mock
    private ExchangeRateRepository exchangeRateRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private BankService bankService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFetchCurrency() {
        LocalDate localDate = LocalDate.now().minusDays(1);
        ExchangeRateDummy exchangeRateDummy = createDummyExchangeRate(localDate);
        ExchangeRateDTO exchangeRateDTO=new ExchangeRateDTO(exchangeRateDummy);

        when(exchangeRateRepository.findByDate(localDate)).thenReturn(List.of(exchangeRateDummy));

        ResponseDTO responseDTO = bankService.fetchCurrency();

        assertEquals(1, responseDTO.getObject().size());
        assertEquals(exchangeRateDTO, responseDTO.getObject().get(0));
    }

    @Test
    public void testFetchTargetCurrency() {
        LocalDate localDate = LocalDate.now().minusDays(1);
        ExchangeRateDummy exchangeRateDummy = createDummyExchangeRate(localDate);
        when(exchangeRateRepository.findByDate(any(LocalDate.class))).thenReturn(List.of(exchangeRateDummy));

        ResponseDTO responseDTO = bankService.fetchTargetCurrency("GBP", 3);

        assertEquals(3, responseDTO.getObject().size());
        responseDTO.getObject().forEach(rate -> {
            assertEquals(1, rate.getRates().size());
            assertEquals(0.85, rate.getRates().get("GBP"));
        });
    }

    @Test
    public void testFetchAndSaveExchangeRate() {
        LocalDate localDate = LocalDate.now().minusDays(1);
        ExchangeRateDummy exchangeRateDummy = createDummyExchangeRate(localDate);

        when(restTemplate.getForObject(anyString(), eq(ExchangeRateDummy.class))).thenReturn(exchangeRateDummy);

        bankService.fetchAndSaveExchangeRate(localDate);

        verify(exchangeRateRepository, times(1)).save(exchangeRateDummy);
    }

    private ExchangeRateDummy createDummyExchangeRate(LocalDate date) {
        ExchangeRateDummy exchangeRateDummy = new ExchangeRateDummy();
        exchangeRateDummy.setDate(date);
        Map<String, Double> rates = new HashMap<>();
        rates.put("USD", 1.0);
        rates.put("GBP", 0.85);
        rates.put("EUR", 0.9);
        rates.put("JPY", 110.0);
        rates.put("CZK", 22.0);
        exchangeRateDummy.setRates(rates);
        return exchangeRateDummy;
    }
}

