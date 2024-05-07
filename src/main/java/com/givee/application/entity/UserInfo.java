package com.givee.application.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "user_info", schema = "wallet")
@Getter
@Setter
@NoArgsConstructor
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "verified", nullable = false)
    private boolean verified = false;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "admin", nullable = false)
    private boolean admin = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserInfo userInfo = (UserInfo) o;
        return id != null && Objects.equals(id, userInfo.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
