package com.example.shop.model;

import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Map;

@Entity
@Data
@ToString
public class ExchangeRateDummy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double amount;
    private String base;
    //unique
    private LocalDate date;

    @ElementCollection
    @CollectionTable(name = "rate_mapping", joinColumns = @JoinColumn(name = "exchange_rate_id"))
    @MapKeyColumn(name = "currency")
    @Column(name = "rate")
    private Map<String, Double> rates;
}

