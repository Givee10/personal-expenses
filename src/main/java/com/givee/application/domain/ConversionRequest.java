package com.givee.application.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class ConversionRequest {
    private String query;
    private String from;
    private String to;
    private BigDecimal amount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversionRequest that = (ConversionRequest) o;
        return Objects.equals(query, that.query) && Objects.equals(from, that.from) && Objects.equals(to, that.to) && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(query, from, to, amount);
    }
}
