package com.givee.application.utils;

import com.givee.application.entity.Currency;
import com.givee.application.entity.CurrencyExchangeRate;
import com.givee.application.entity.UserInfo;
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
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";

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

    public static Specification<UserInfo> filterUserInfo(String username, String email, String lastName, String firstName, Boolean enabled) {
        return (Root<UserInfo> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Add filtering criteria based on provided parameters
            if (StringUtils.isNotBlank(username)) {
                predicates.add(likeIgnoreCase(criteriaBuilder, root.get(USERNAME), username));
            }
            if (StringUtils.isNotBlank(lastName)) {
                predicates.add(likeIgnoreCase(criteriaBuilder, root.get(LAST_NAME), lastName));
            }
            if (StringUtils.isNotBlank(firstName)) {
                predicates.add(likeIgnoreCase(criteriaBuilder, root.get(FIRST_NAME), firstName));
            }
            if (StringUtils.isNotBlank(email)) {
                predicates.add(likeIgnoreCase(criteriaBuilder, root.get(EMAIL), email));
            }
            if (enabled != null) {
                predicates.add(criteriaBuilder.equal(root.get(ENABLED), enabled));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static Predicate likeIgnoreCase(CriteriaBuilder criteriaBuilder, Path<String> path, String value) {
        return criteriaBuilder.like(criteriaBuilder.lower(path), value.toLowerCase());
    }
}
