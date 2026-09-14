package com.drinkIt.service;

import java.util.List;

import com.drinkIt.dto.category.CategoryRequest;
import com.drinkIt.dto.category.CategoryResponse;

public interface CategoryService {

    CategoryResponse create(CategoryRequest request);

    List<CategoryResponse> getAll();

    List<CategoryResponse> getActive();

    CategoryResponse getById(Long id);

    CategoryResponse update(
            Long id,
            CategoryRequest request
    );

    void delete(Long id);
}