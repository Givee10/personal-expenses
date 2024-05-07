package com.givee.application.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "user_exchange", schema = "wallet")
@Getter
@Setter
@NoArgsConstructor
public class UserExchange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_currency_id", nullable = false)
    private Currency fromCurrency;

    @ManyToOne
    @JoinColumn(name = "to_currency_id", nullable = false)
    private Currency toCurrency;

    @Column(name = "from_amount", nullable = false)
    private BigDecimal fromAmount;

    @Column(name = "to_amount", nullable = false)
    private BigDecimal toAmount;

    @Column(name = "rate", nullable = false)
    private BigDecimal rate;

    @Column(name = "exchange_date", nullable = false)
    private LocalDate exchangeDate;

    @Column(name = "notes", nullable = false)
    private String notes;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfo userInfo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserExchange that = (UserExchange) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
