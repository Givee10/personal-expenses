package com.givee.application.repository;

import com.givee.application.entity.Currency;
import com.givee.application.entity.CurrencyExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CurrencyExchangeRateRepository extends JpaRepository<CurrencyExchangeRate, Long>, JpaSpecificationExecutor<CurrencyExchangeRate> {
    CurrencyExchangeRate findByToCurrencyAndExchangeDate(@NonNull Currency toCurrency, @NonNull LocalDate date);
}