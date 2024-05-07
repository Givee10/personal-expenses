package com.givee.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageToTextService {
    private final ITesseract tesseract;

    public String extractTextFromMultiPartFile(MultipartFile multipartFile, String language) throws IOException {
        return extractTextFromFile(convertMultipartFileToFile(multipartFile), language, true);
    }

    public String extractTextFromFile(File file, String language, boolean deleted) {
        // Check if the image file exists
        if (!file.exists()) {
            throw new IllegalArgumentException("Image file not found: " + file);
        }

        // Perform OCR on the image
        String result = null;
        try {
            tesseract.setLanguage(language);
            result = tesseract.doOCR(file);
        } catch (TesseractException e) {
            log.error(e.getMessage());
        } finally {
            if (deleted) {
                log.info("File {} deleted: {}", file.getName(), file.delete());
            } else {
                log.info("File {} was not deleted for test purposes", file.getName());
            }
        }
        return result;
    }

    public String extractTextFromFile(File file, String language) {
        return extractTextFromFile(file, language, false);
    }

    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        // Create a File object
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));

        // Get the input stream from the MultipartFile
        try (InputStream inputStream = multipartFile.getInputStream()) {
            // Copy the input stream to the File
            Files.copy(inputStream, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        return file;
    }
}
