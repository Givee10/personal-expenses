package com.givee.application.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class ConversionMeta {
    private Long timestamp;
    private BigDecimal rate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversionMeta that = (ConversionMeta) o;
        return Objects.equals(timestamp, that.timestamp) && Objects.equals(rate, that.rate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, rate);
    }
}
