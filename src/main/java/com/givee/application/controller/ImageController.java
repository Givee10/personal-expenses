package com.givee.application.controller;

import com.givee.application.domain.PingDTO;
import com.givee.application.service.ImageToTextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/image")
@RequiredArgsConstructor
@Slf4j
public class ImageController {
    private final ImageToTextService service;

    @PostMapping("/convert")
    public ResponseEntity<Object> convertImage(
            @RequestPart("file") MultipartFile file,
            @RequestParam(defaultValue = "eng") String lang) {
        try {
            return ResponseEntity.ok(new PingDTO(service.extractTextFromMultiPartFile(file, lang)));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
