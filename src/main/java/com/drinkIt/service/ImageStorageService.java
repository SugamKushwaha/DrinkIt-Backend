package com.drinkIt.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {

    String saveCategoryImage(MultipartFile file);

    void deleteImage(String imagePath);
}