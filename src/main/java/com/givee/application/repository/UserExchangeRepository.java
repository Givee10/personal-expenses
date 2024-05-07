package com.givee.application.repository;

import com.givee.application.entity.Currency;
import com.givee.application.entity.UserExchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface UserExchangeRepository extends JpaRepository<UserExchange, Long>, JpaSpecificationExecutor<UserExchange> {
    UserExchange findByFromCurrencyAndToCurrencyAndExchangeDate(
            @NonNull Currency fromCurrency, @NonNull Currency toCurrency, @NonNull LocalDate date);
}