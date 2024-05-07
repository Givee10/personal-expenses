package com.givee.application.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "receipts", schema = "wallet")
@Getter
@Setter
@NoArgsConstructor
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "receipt_number", length = 50, nullable = false)
    private String receiptNumber;

    @Column(name = "receipt_date", nullable = false)
    private LocalDate receiptDate;

    @Column(name = "store_name", nullable = false)
    private String storeName;

    @Column(name = "total_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "payment_method", length = 50, nullable = false)
    private String paymentMethod;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "receipt", cascade = CascadeType.ALL)
    private List<Item> items;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserInfo userInfo;
}
