package com.givee.application.controller;

import com.givee.application.domain.PingDTO;
import com.givee.application.domain.UserInfoDTO;
import com.givee.application.entity.UserInfo;
import com.givee.application.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserInfoController {
    private final UserInfoRepository userInfoRepository;
    private final ModelMapper modelMapper;

    // Create a new user
    @PostMapping
    public ResponseEntity<UserInfoDTO> createUser(@RequestBody UserInfoDTO userInfoDTO) {
        UserInfo userInfo = modelMapper.map(userInfoDTO, UserInfo.class);
        UserInfo savedUserInfo = userInfoRepository.save(userInfo);
        UserInfoDTO savedUserInfoDTO = modelMapper.map(savedUserInfo, UserInfoDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUserInfoDTO);
    }

    // Read all users
    @GetMapping
    public ResponseEntity<List<UserInfoDTO>> getAllUsers() {
        List<UserInfo> users = userInfoRepository.findAll();
        List<UserInfoDTO> userDTOs = users.stream()
                .map(user -> modelMapper.map(user, UserInfoDTO.class))
                .toList();
        return ResponseEntity.ok(userDTOs);
    }

    // Read a single user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserInfoDTO> getUserById(@PathVariable Long id) {
        Optional<UserInfo> userInfo = userInfoRepository.findById(id);
        if (userInfo.isPresent()) {
            UserInfoDTO userInfoDTO = modelMapper.map(userInfo.get(), UserInfoDTO.class);
            return ResponseEntity.ok(userInfoDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Update an existing user by ID
    @PutMapping("/{id}")
    public ResponseEntity<UserInfoDTO> updateUser(@PathVariable Long id, @RequestBody UserInfoDTO userInfoDTO) {
        if (userInfoRepository.existsById(id)) {
            UserInfo userInfo = modelMapper.map(userInfoDTO, UserInfo.class);
            userInfo.setId(id);
            UserInfo savedItem = userInfoRepository.save(userInfo);
            return ResponseEntity.ok(modelMapper.map(savedItem, UserInfoDTO.class));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
        if (userInfoRepository.existsById(id)) {
            userInfoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Find current user with authentication
    @GetMapping("/find")
    public ResponseEntity<Object> findUser(@RequestParam String username, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (StringUtils.isNotEmpty(username) && username.equalsIgnoreCase(userDetails.getUsername())) {
            return ResponseEntity.ok(userDetails);
        } else {
            String message = "Username " + username + " is not logged in";
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new PingDTO(message));
        }
    }

    // Find user by username
    @GetMapping("/search")
    public ResponseEntity<Object> findUser(@RequestParam String username) {
        UserInfo userInfo = userInfoRepository.findByUsernameIgnoreCase(username);
        if (userInfo != null) {
            return ResponseEntity.ok(modelMapper.map(userInfo, UserInfoDTO.class));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
