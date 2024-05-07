package com.givee.application.controller;

import com.givee.application.domain.PingDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ping")
public class PingController {
    @GetMapping
    public ResponseEntity<Object> checkService() {
        return ResponseEntity.ok(new PingDTO("Service is working"));
    }
}
