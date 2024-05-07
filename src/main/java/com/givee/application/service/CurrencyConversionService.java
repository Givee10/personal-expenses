package com.givee.application.service;

import com.givee.application.client.OpenExchangeRatesClient;
import com.givee.application.domain.ConversionResponse;
import com.givee.application.domain.RatesResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CurrencyConversionService {
    private final OpenExchangeRatesClient client;

    public BigDecimal convertCurrency(String fromCurrency, String toCurrency, BigDecimal amount) {
        // Extract the conversion result from the response
        ConversionResponse conversionResponse = client.convertCurrency(fromCurrency, toCurrency, amount);

        if (conversionResponse != null) {
            return conversionResponse.getResponse();
        } else {
            // Handle the error or return an appropriate value
            return BigDecimal.ZERO;
        }
    }

    public Map<String, BigDecimal> getCurrentRates() {
        // Extract the conversion result from the response
        RatesResponse ratesResponse = client.getCurrentRates();

        if (ratesResponse != null) {
            return ratesResponse.getRates();
        } else {
            // Handle the error or return an appropriate value
            return Map.of();
        }
    }

    public Map<String, BigDecimal> getHistoricalRates(LocalDate date) {
        // Extract the conversion result from the response
        RatesResponse ratesResponse = client.getHistoricalRates(date);

        if (ratesResponse != null) {
            return ratesResponse.getRates();
        } else {
            // Handle the error or return an appropriate value
            return Map.of();
        }
    }

    public Map<String, String> getCurrencies() {
        return client.getCurrencies();
    }

    public RestTemplate getRestTemplate() {
        return client.getRestTemplate();
    }
}
