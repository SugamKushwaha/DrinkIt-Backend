package com.drinkIt.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.drinkIt.dto.category.CategoryResponse;
import com.drinkIt.service.CategoryService;

import lombok.RequiredArgsConstructor;

// =====================================================
// PUBLIC - CUSTOMER-FACING CATEGORY ENDPOINTS
//
// Mapped to "/api/categories", which is already
// permitAll() in SecurityConfig — no auth required.
// This is separate from AdminCategoryController
// ("/api/admin/categories"), which stays ADMIN-only.
// =====================================================

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // =====================================================
    // GET ALL CATEGORIES (for the shop homepage)
    // GET /api/categories
    // =====================================================

    @GetMapping
    public List<CategoryResponse> getAllCategories() {
        return categoryService.getAllImages();
    }
}
