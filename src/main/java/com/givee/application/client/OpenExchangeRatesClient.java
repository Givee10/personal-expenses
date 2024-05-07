package com.givee.application.client;

import com.givee.application.domain.ConversionResponse;
import com.givee.application.domain.RatesResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class OpenExchangeRatesClient {
    @Getter
    private final RestTemplate restTemplate;

    @Value("${openexchangerates.api.baseurl}")
    private String baseUrl;

    @Value("${openexchangerates.api.appid}")
    private String appId;

    private static final String APP_PARAM = "app_id";

    public ConversionResponse convertCurrency(String fromCurrency, String toCurrency, BigDecimal amount) {
        // Build the API URL with the required parameters
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/convert/")
                .path(amount.toPlainString()).path("/")
                .path(fromCurrency).path("/")
                .path(toCurrency)
                .queryParam(APP_PARAM, appId);

        // Make a GET request to the API
        return makeGetRequest(builder.toUriString(), ConversionResponse.class);
    }

    public RatesResponse getCurrentRates() {
        // Build the API URL with the required parameters
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/latest.json")
                .queryParam(APP_PARAM, appId);

        // Make a GET request to the API
        return makeGetRequest(builder.toUriString(), RatesResponse.class);
    }

    public RatesResponse getHistoricalRates(LocalDate date) {
        // Make sure that date is not null
        if (date == null) {
            return null;
        }
        // Build the API URL with the required parameters
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/historical/")
                .path(date + ".json")
                .queryParam(APP_PARAM, appId);

        // Make a GET request to the API
        return makeGetRequest(builder.toUriString(), RatesResponse.class);
    }

    public Map<String, String> getCurrencies() {
        // Build the API URL with the required parameters
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/currencies.json")
                .queryParam(APP_PARAM, appId)
                .queryParam("show_inactive", "true");

        // Make a GET request to the API
        return makeGetRequest(builder.toUriString(), new ParameterizedTypeReference<>() {});
    }

    private <T> T makeGetRequest(String url, Class<T> tClass) {
        try {
            ResponseEntity<T> response = restTemplate.getForEntity(url, tClass);
            if (response.hasBody()) {
                return response.getBody();
            }
        } catch (RestClientResponseException e) {
            log.error(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private <T> T makeGetRequest(String url, ParameterizedTypeReference<T> responseType) {
        try {
            ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            if (response.hasBody()) {
                return response.getBody();
            }
        } catch (RestClientResponseException e) {
            // Handle RestClientResponseException, log error, and return an appropriate response
            log.error(e.getResponseBodyAsString());
        } catch (Exception e) {
            // Handle other exceptions, log error, and return an internal server error response
            log.error(e.getMessage());
        }
        return null;
    }
}
