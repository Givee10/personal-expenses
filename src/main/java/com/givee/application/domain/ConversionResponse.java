package com.givee.application.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class ConversionResponse {
    private String disclaimer;
    private String license;
    private ConversionRequest request;
    private ConversionMeta meta;
    private BigDecimal response;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversionResponse that = (ConversionResponse) o;
        return Objects.equals(disclaimer, that.disclaimer) && Objects.equals(license, that.license) && Objects.equals(request, that.request) && Objects.equals(meta, that.meta) && Objects.equals(response, that.response);
    }

    @Override
    public int hashCode() {
        return Objects.hash(disclaimer, license, request, meta, response);
    }
}
