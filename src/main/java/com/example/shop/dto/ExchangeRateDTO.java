package com.example.shop.dto;

import com.example.shop.model.ExchangeRateDummy;
import lombok.Data;

import java.time.LocalDate;
import java.util.Map;

@Data
public class ExchangeRateDTO {
    private double amount;
    private String base;
    private LocalDate date;
    private Map<String, Double> rates;

    // Constructor that accepts an ExchangeRateDummy object
    public ExchangeRateDTO(ExchangeRateDummy exchangeRateDummy) {
        this.amount = exchangeRateDummy.getAmount();
        this.base = exchangeRateDummy.getBase();
        this.date = exchangeRateDummy.getDate();
        this.rates = exchangeRateDummy.getRates();
    }
}
