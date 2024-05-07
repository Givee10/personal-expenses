package com.givee.application.controller;

import com.givee.application.domain.CurrencyDTO;
import com.givee.application.domain.CurrencyExchangeRateDTO;
import com.givee.application.domain.UserExchangeDTO;
import com.givee.application.entity.Currency;
import com.givee.application.entity.CurrencyExchangeRate;
import com.givee.application.entity.UserExchange;
import com.givee.application.repository.CurrencyExchangeRateRepository;
import com.givee.application.repository.CurrencyRepository;
import com.givee.application.repository.UserExchangeRepository;
import com.givee.application.utils.ConvertUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.givee.application.utils.SpecificationUtils.filterCurrency;
import static com.givee.application.utils.SpecificationUtils.filterCurrencyExchangeRates;

@RestController
@RequestMapping("/api/v1/exchanges")
@RequiredArgsConstructor
public class ExchangeController {
    private final CurrencyRepository currencyRepository;
    private final CurrencyExchangeRateRepository exchangeRateRepository;
    private final UserExchangeRepository userExchangeRepository;
    private final ModelMapper modelMapper;

    // Read exchange rates with possible filters
    @GetMapping
    public ResponseEntity<Object> getExchanges(
            @RequestParam(defaultValue = "USD") String fromCode,
            @RequestParam(defaultValue = "") List<String> toCode,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            Pageable pageable
    ) {
        Page<CurrencyExchangeRate> exchangeRates = exchangeRateRepository.findAll(
                filterCurrencyExchangeRates(fromCode, toCode, startDate, endDate), pageable);
        return ResponseEntity.ok(ConvertUtils.convertPageToList(modelMapper, exchangeRates, CurrencyExchangeRateDTO.class));
    }

    // Read currencies with possible filters
    @GetMapping("/currency")
    public ResponseEntity<Object> getCurrencies(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) Boolean enabled
    ) {
        List<Currency> currencies = currencyRepository.findAll(filterCurrency(code, enabled));
        List<CurrencyDTO> currencyDTOList = ConvertUtils.convertLists(modelMapper, currencies, CurrencyDTO.class);
        return ResponseEntity.ok(currencyDTOList);
    }

    @GetMapping("/rate")
    public ResponseEntity<CurrencyExchangeRateDTO> getExchangeRateForDate(@RequestParam String code, @RequestParam LocalDate date) {
        Currency currency = currencyRepository.findByCode(code);
        if (currency != null) {
            CurrencyExchangeRate exchangeRate = exchangeRateRepository.findByToCurrencyAndExchangeDate(currency, date);
            if (exchangeRate != null) {
                return ResponseEntity.ok(modelMapper.map(exchangeRate, CurrencyExchangeRateDTO.class));
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/convert")
    public ResponseEntity<CurrencyExchangeRateDTO> convertRateForDate(
            @RequestParam String fromCode, @RequestParam String toCode, @RequestParam LocalDate date
    ) {
        Currency fromCurrency = currencyRepository.findByCode(fromCode);
        Currency toCurrency = currencyRepository.findByCode(toCode);
        if (fromCurrency != null && toCurrency != null) {
            CurrencyExchangeRate fromExchangeRate = exchangeRateRepository.findByToCurrencyAndExchangeDate(fromCurrency, date);
            CurrencyExchangeRate toExchangeRate = exchangeRateRepository.findByToCurrencyAndExchangeDate(toCurrency, date);
            if (fromExchangeRate != null && toExchangeRate != null) {
                return ResponseEntity.ok(createExchange(fromExchangeRate, toExchangeRate));
            }
        }
        return ResponseEntity.notFound().build();
    }

    // Create a new UserExchange
    @PostMapping("/users")
    public ResponseEntity<UserExchangeDTO> createUserExchange(@RequestBody UserExchangeDTO userExchangeDTO) {
        // Convert UserExchangeDTO to UserExchange entity
        UserExchange userExchange = modelMapper.map(userExchangeDTO, UserExchange.class);
        UserExchange savedUserExchange = userExchangeRepository.save(userExchange);
        // Convert the saved UserExchange entity back to UserExchangeDTO
        UserExchangeDTO savedUserExchangeDTO = modelMapper.map(savedUserExchange, UserExchangeDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUserExchangeDTO);
    }

    // Read all UserExchanges
    @GetMapping("/users")
    public ResponseEntity<List<UserExchangeDTO>> getAllUserExchanges() {
        List<UserExchange> userExchanges = userExchangeRepository.findAll();
        return ResponseEntity.ok(ConvertUtils.convertLists(modelMapper, userExchanges, UserExchangeDTO.class));
    }

    // Read a single UserExchange by ID
    @GetMapping("/users/{id}")
    public ResponseEntity<UserExchangeDTO> getUserExchangeById(@PathVariable Long id) {
        Optional<UserExchange> userExchange = userExchangeRepository.findById(id);
        if (userExchange.isPresent()) {
            UserExchangeDTO userExchangeDTO = modelMapper.map(userExchange.get(), UserExchangeDTO.class);
            return ResponseEntity.ok(userExchangeDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Update a UserExchange by ID
    @PutMapping("/users/{id}")
    public ResponseEntity<UserExchangeDTO> updateUserExchange(@PathVariable Long id, @RequestBody UserExchangeDTO updatedUserExchange) {
        if (userExchangeRepository.existsById(id)) {
            UserExchange userExchange = modelMapper.map(updatedUserExchange, UserExchange.class);
            userExchange.setId(id);
            UserExchange savedUserExchange = userExchangeRepository.save(userExchange);
            return ResponseEntity.ok(modelMapper.map(savedUserExchange, UserExchangeDTO.class));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a UserExchange by ID
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUserExchange(@PathVariable Long id) {
        if (userExchangeRepository.existsById(id)) {
            userExchangeRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private CurrencyExchangeRateDTO createExchange(CurrencyExchangeRate fromExchangeRate, CurrencyExchangeRate toExchangeRate) {
        CurrencyExchangeRateDTO fromRate = modelMapper.map(fromExchangeRate, CurrencyExchangeRateDTO.class);
        CurrencyExchangeRateDTO toRate = modelMapper.map(toExchangeRate, CurrencyExchangeRateDTO.class);
        CurrencyExchangeRateDTO exchangeRateDTO = new CurrencyExchangeRateDTO();
        exchangeRateDTO.setExchangeDate(fromRate.getExchangeDate());
        exchangeRateDTO.setFromCurrency(fromRate.getToCurrency());
        exchangeRateDTO.setToCurrency(toRate.getToCurrency());
        exchangeRateDTO.setRate(toRate.getRate().divide(fromRate.getRate(), RoundingMode.CEILING));
        exchangeRateDTO.setCreatedAt(fromRate.getCreatedAt());
        exchangeRateDTO.setUpdatedAt(fromRate.getUpdatedAt());
        return exchangeRateDTO;
    }
}
