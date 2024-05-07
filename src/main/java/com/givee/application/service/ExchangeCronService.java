package com.givee.application.service;

import com.givee.application.client.OpenExchangeRatesClient;
import com.givee.application.domain.RatesResponse;
import com.givee.application.entity.Currency;
import com.givee.application.entity.CurrencyExchangeRate;
import com.givee.application.repository.CurrencyExchangeRateRepository;
import com.givee.application.repository.CurrencyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExchangeCronService {
    private final OpenExchangeRatesClient client;
    private final CurrencyRepository currencyRepository;
    private final CurrencyExchangeRateRepository exchangeRateRepository;

    private static final String LOG_MESSAGE = "Job finished in {} seconds";
    private static final int SCHEDULE_RATE = 24 * 60 * 60 * 1000;

//    @Scheduled(cron = "0 0 5 * * ?")
//    @Scheduled(fixedRate = SCHEDULE_RATE)
    public void saveCurrencies() {
        StopWatch stopWatch = new StopWatch(this.getClass().getSimpleName());
        stopWatch.start();
        log.info("Getting currencies");
        Map<String, String> currencies = client.getCurrencies();
        currencies.forEach((key, value) -> {
            Currency currency = currencyRepository.findByCode(key);
            if (currency == null) {
                currency = new Currency();
            }
            currency.setCode(key);
            currency.setName(value);
            currencyRepository.save(currency);
        });
        stopWatch.stop();
        log.info(LOG_MESSAGE, stopWatch.getTotalTimeSeconds());
    }

//    @Scheduled(cron = "0 5 22 * * ?")
//    @Scheduled(fixedRate = SCHEDULE_RATE)
    public void getHistoricalRates() {
        StopWatch stopWatch = new StopWatch(this.getClass().getSimpleName());
        stopWatch.start();
        log.info("Getting rates from past days");
        LocalDate start = LocalDate.of(2023, 9, 26);
        LocalDate finish = LocalDate.of(2023, 10, 12);
        while (start.isBefore(finish)) {
            saveRates(start);
            start = start.plusDays(1);
        }
        stopWatch.stop();
        log.info(LOG_MESSAGE, stopWatch.getTotalTimeSeconds());
    }

//    @Scheduled(cron = "0 0 6 * * ?")
//    @Scheduled(fixedRate = SCHEDULE_RATE)
    public void getYesterdayRates() {
        StopWatch stopWatch = new StopWatch(this.getClass().getSimpleName());
        stopWatch.start();
        log.info("Getting rates from yesterday");
        LocalDate date = LocalDate.now().minusDays(1);
        saveRates(date);
        stopWatch.stop();
        log.info(LOG_MESSAGE, stopWatch.getTotalTimeSeconds());
    }

    private void saveRates(LocalDate date) {
        log.info(date.toString());
        RatesResponse historicalRates = client.getHistoricalRates(date);
        Currency baseCurrency = currencyRepository.findByCode(historicalRates.getBase());
        historicalRates.getRates().forEach((key, value) -> {
            Currency currency = currencyRepository.findByCode(key);
            if (currency != null) {
                CurrencyExchangeRate rate = exchangeRateRepository.findByToCurrencyAndExchangeDate(currency, date);
                if (rate == null) {
                    rate = new CurrencyExchangeRate();
                    rate.setCreatedAt(LocalDateTime.now());
                }
                rate.setExchangeDate(date);
                rate.setFromCurrency(baseCurrency);
                rate.setToCurrency(currency);
                rate.setRate(value);
                rate.setUpdatedAt(LocalDateTime.now());
                exchangeRateRepository.save(rate);
            } else {
                log.warn("Unable to find currency with code {}", key);
            }
        });
    }
}
