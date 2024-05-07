package com.givee.application.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ItemDTO {
    private Long id;
    private String itemName;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}
