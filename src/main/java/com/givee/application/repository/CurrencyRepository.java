package com.givee.application.repository;

import com.givee.application.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Long>, JpaSpecificationExecutor<Currency> {
    Currency findByCode(@NonNull String code);
}