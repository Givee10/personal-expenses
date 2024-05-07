package com.givee.application.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReceiptDTO {
    private Long id;
    private String receiptNumber;
    private LocalDate receiptDate;
    private String storeName;
    private BigDecimal totalAmount;
    private String paymentMethod;
    private LocalDateTime createdAt;
    private UserInfoDTO userInfo;
}