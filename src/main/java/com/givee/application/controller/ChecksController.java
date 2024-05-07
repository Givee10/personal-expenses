package com.givee.application.controller;

import com.givee.application.domain.PingDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/checks")
public class ChecksController {
    @GetMapping
    public ResponseEntity<Object> getAllChecks() {
        return ResponseEntity.ok(new PingDTO("getAllChecks"));
    }

    @PostMapping
    public ResponseEntity<Object> createCheck() {
        return ResponseEntity.ok(new PingDTO("createCheck"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getCheck(@PathVariable Long id) {
        return ResponseEntity.ok(new PingDTO("getCheck(" + id + ")"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCheck(@PathVariable Long id) {
        return ResponseEntity.ok(new PingDTO("updateCheck(" + id + ")"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCheck(@PathVariable Long id) {
        return ResponseEntity.ok(new PingDTO("deleteCheck(" + id + ")"));
    }
}
