package com.online_marketplace_api.awesomity.service.impl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {

    // Path to store files (this can be modified to point to a cloud storage path)
    @Value("${file.upload-dir}")
    private String uploadDir;

    public String storeFile(MultipartFile file) {
        // Create a unique file name
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        try {
            // Define the path where the file will be saved
            Path path = Paths.get(uploadDir + File.separator + fileName);

            // Create the directory if it does not exist
            Files.createDirectories(path.getParent());

            // Copy the file to the target location
            file.transferTo(path.toFile());

            // Return the relative path or URL of the saved file
            return path.toString();
        } catch (IOException e) {
            throw new RuntimeException("Could not store file. Please try again!", e);
        }
    }

    // Method to retrieve file (optional)
    public byte[] loadFileAsBytes(String fileName) throws IOException {
        Path path = Paths.get(uploadDir + File.separator + fileName);
        return Files.readAllBytes(path);
    }
}
