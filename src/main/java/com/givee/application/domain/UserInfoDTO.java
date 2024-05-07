package com.givee.application.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserInfoDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private boolean verified;
    private boolean enabled;
    private boolean admin;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
}
