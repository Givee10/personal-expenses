package com.givee.application.security;

import com.givee.application.domain.RoleName;
import com.givee.application.entity.UserInfo;
import com.givee.application.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserInfoRepository repository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = repository.findByUsernameIgnoreCase(username);
        if (userInfo != null) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(RoleName.ROLE_USER.name()));
            if (userInfo.isAdmin()) {
                authorities.add(new SimpleGrantedAuthority(RoleName.ROLE_ADMIN.name()));
            }
            return new org.springframework.security.core.userdetails.User(userInfo.getUsername(), userInfo.getPassword(), userInfo.isEnabled(),
                    userInfo.isEnabled(), userInfo.isEnabled(), userInfo.isEnabled(), authorities);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
