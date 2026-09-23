package com.drinkIt.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.drinkIt.dto.category.CategoryResponse;

public interface CategoryService {

    CategoryResponse uploadImage(
            String categoryName,
            MultipartFile image
    );

    List<CategoryResponse> getAllImages();

    CategoryResponse getImageByCategoryName(
            String categoryName
    );

    void deleteImage(Long id);
}