package com.givee.application.security;

import com.givee.application.entity.UserInfo;
import com.givee.application.repository.UserInfoRepository;
import com.vaadin.flow.spring.security.AuthenticationContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthenticatedUser {
    private final UserInfoRepository userRepository;
    private final AuthenticationContext authenticationContext;

    @Transactional
    public Optional<UserInfo> get() {
        return authenticationContext.getAuthenticatedUser(UserDetails.class)
                .map(userDetails -> userRepository.findByUsernameIgnoreCase(userDetails.getUsername()));
    }

    public void logout() {
        authenticationContext.logout();
    }

}
