package com.givee.application.domain;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class RatesResponse {
    private String disclaimer;
    private String license;
    private Long timestamp;
    private String base;

    @JsonAnyGetter
    @JsonAnySetter
    private Map<String, BigDecimal> rates = new HashMap<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RatesResponse that = (RatesResponse) o;
        return Objects.equals(disclaimer, that.disclaimer) && Objects.equals(license, that.license) && Objects.equals(timestamp, that.timestamp) && Objects.equals(base, that.base) && Objects.equals(rates, that.rates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(disclaimer, license, timestamp, base, rates);
    }
}
