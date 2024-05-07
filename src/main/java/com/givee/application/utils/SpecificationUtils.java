package com.givee.application.utils;

import com.givee.application.entity.Currency;
import com.givee.application.entity.CurrencyExchangeRate;
import jakarta.persistence.criteria.*;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class SpecificationUtils {
    public static final String FROM_CURRENCY = "fromCurrency";
    public static final String TO_CURRENCY = "toCurrency";
    public static final String EXCHANGE_DATE = "exchangeDate";
    public static final String CODE = "code";
    public static final String ENABLED = "enabled";

    public static Specification<CurrencyExchangeRate> filterCurrencyExchangeRates(
            String fromCode, List<String> toCode, LocalDate startDate, LocalDate endDate) {
        return (Root<CurrencyExchangeRate> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            // Add fetches to make Pageable requests work properly
            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch(FROM_CURRENCY, JoinType.INNER);
                root.fetch(TO_CURRENCY, JoinType.INNER);
            } else {
                root.join(FROM_CURRENCY, JoinType.INNER);
                root.join(TO_CURRENCY, JoinType.INNER);
            }

            List<Predicate> predicates = new ArrayList<>();

            // Add filtering criteria based on provided parameters
            if (startDate != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(EXCHANGE_DATE), startDate));
            }
            if (endDate != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(EXCHANGE_DATE), endDate));
            }
            if (StringUtils.isNotEmpty(fromCode)) {
                predicates.add(criteriaBuilder.equal(root.get(FROM_CURRENCY).get(CODE), fromCode));
            }
            if (toCode != null && !toCode.isEmpty()) {
                predicates.add(root.get(TO_CURRENCY).get(CODE).in(toCode));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Currency> filterCurrency(String code, Boolean enabled) {
        return (Root<Currency> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Add filtering criteria based on provided parameters
            if (StringUtils.isNotEmpty(code)) {
                predicates.add(criteriaBuilder.equal(root.get(CODE), code));
            }
            if (enabled != null) {
                predicates.add(criteriaBuilder.equal(root.get(ENABLED), enabled));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
