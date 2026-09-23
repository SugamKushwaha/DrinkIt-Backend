package com.drinkIt.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.drinkIt.service.ImageStorageService;

@Service
public class ImageStorageServiceImpl
        implements ImageStorageService {

    private final Path categoryUploadPath =
            Paths.get("uploads/categories");


            @Override
public String saveCategoryImage(MultipartFile file) {

    if (file == null || file.isEmpty()) {
        throw new RuntimeException("Category image is required");
    }

    try {

        Path uploadPath = Paths.get("uploads/categories");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFileName = file.getOriginalFilename();

        String extension = "";

        if (originalFileName != null &&
                originalFileName.contains(".")) {

            extension = originalFileName.substring(
                    originalFileName.lastIndexOf(".")
            );
        }

        String fileName =
                UUID.randomUUID() + extension;

        Path filePath =
                uploadPath.resolve(fileName);

        Files.copy(
                file.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING
        );

        return "/uploads/categories/" + fileName;

    } catch (IOException e) {

        throw new RuntimeException(
                "Failed to save category image",
                e
        );
    }
}

    @Override
    public void deleteImage(
            String imagePath
    ) {

        if (imagePath == null
                || imagePath.isBlank()) {

            return;
        }

        try {

            String cleanPath =
                    imagePath.startsWith("/")
                            ? imagePath.substring(1)
                            : imagePath;

            Path path =
                    Paths.get(cleanPath);

            Files.deleteIfExists(path);

        } catch (IOException e) {

            System.err.println(
                    "Unable to delete image: "
                            + imagePath
            );
        }
    }
}