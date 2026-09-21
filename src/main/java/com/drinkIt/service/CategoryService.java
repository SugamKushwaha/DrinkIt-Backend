package com.drinkIt.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.drinkIt.dto.category.CategoryResponse;

public interface CategoryService {

    CategoryResponse create(
            String name,
            MultipartFile image,
            Boolean active
    );

    List<CategoryResponse> getAll();

    List<CategoryResponse> getActive();

    CategoryResponse getById(
            Long id
    );

    CategoryResponse update(
            Long id,
            String name,
            MultipartFile image,
            Boolean active
    );

    void delete(
            Long id
    );
}