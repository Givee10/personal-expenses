package com.givee.application.controller;

import com.givee.application.configuration.JwtTokenProvider;
import com.givee.application.domain.LoginRequest;
import com.givee.application.domain.LoginResponse;
import com.givee.application.domain.PingDTO;
import com.givee.application.domain.UserInfoDTO;
import com.givee.application.entity.UserInfo;
import com.givee.application.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserInfoRepository userInfoRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<Object> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.generateToken(authentication);
            return ResponseEntity.ok(new LoginResponse(jwt));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().body(new PingDTO(e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody UserInfoDTO userInfoDTO) {
        if (StringUtils.isEmpty(userInfoDTO.getUsername())) {
            return ResponseEntity.badRequest().body(new PingDTO("Username cannot be empty!"));
        }
        if (StringUtils.isEmpty(userInfoDTO.getEmail())) {
            return ResponseEntity.badRequest().body(new PingDTO("Email cannot be empty!"));
        }
        if (userInfoRepository.existsByUsernameIgnoreCase(userInfoDTO.getUsername())) {
            return ResponseEntity.badRequest().body(new PingDTO("Username is already taken!"));
        }
        if (userInfoRepository.existsByEmailIgnoreCase(userInfoDTO.getEmail())) {
            return ResponseEntity.badRequest().body(new PingDTO("Email is already taken!"));
        }

        // Create a new user account
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(userInfoDTO.getUsername());
        userInfo.setPassword(passwordEncoder.encode(userInfoDTO.getPassword()));
        userInfo.setEmail(userInfoDTO.getEmail());
        userInfo.setFirstName(userInfoDTO.getFirstName());
        userInfo.setLastName(userInfoDTO.getLastName());
        UserInfo saved = userInfoRepository.save(userInfo);
        return ResponseEntity.ok(modelMapper.map(saved, UserInfoDTO.class));
    }
}
