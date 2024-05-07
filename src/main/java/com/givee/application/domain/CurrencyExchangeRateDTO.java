package com.givee.application.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class CurrencyExchangeRateDTO {
    private Long id;
    private CurrencyDTO fromCurrency;
    private CurrencyDTO toCurrency;
    private BigDecimal rate;
    private LocalDate exchangeDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
