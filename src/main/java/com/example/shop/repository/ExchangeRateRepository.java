package com.example.shop.repository;

import com.example.shop.model.ExchangeRateDummy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExchangeRateRepository extends CrudRepository<ExchangeRateDummy, Long> {
    List<ExchangeRateDummy> findByDate(LocalDate exchangeRateDate);

}