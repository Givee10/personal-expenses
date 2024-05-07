package com.givee.application.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CurrencyDTO {
    private Long id;
    private String code;
    private String name;
    private boolean enabled;
}
