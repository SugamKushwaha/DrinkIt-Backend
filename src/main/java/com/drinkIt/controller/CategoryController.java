package com.drinkIt.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.drinkIt.dto.category.CategoryRequest;
import com.drinkIt.dto.category.CategoryResponse;
import com.drinkIt.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    // =====================================================
    // CUSTOMER
    // =====================================================

    @GetMapping
    public ResponseEntity<List<CategoryResponse>>
    getActiveCategories() {

        return ResponseEntity.ok(
                categoryService.getActive()
        );
    }

    // =====================================================
    // ADMIN - GET ALL
    // =====================================================

    @GetMapping("/admin")
    public ResponseEntity<List<CategoryResponse>>
    getAllCategories() {

        return ResponseEntity.ok(
                categoryService.getAll()
        );
    }

    // =====================================================
    // ADMIN - GET ONE
    // =====================================================

    @GetMapping("/admin/{id}")
    public ResponseEntity<CategoryResponse>
    getCategory(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                categoryService.getById(id)
        );
    }

    // =====================================================
    // ADMIN - CREATE
    // =====================================================

    @PostMapping("/admin")
    public ResponseEntity<CategoryResponse>
    createCategory(
            @RequestBody CategoryRequest request
    ) {

        return ResponseEntity.ok(
                categoryService.create(request)
        );
    }

    // =====================================================
    // ADMIN - UPDATE
    // =====================================================

    @PutMapping("/admin/{id}")
    public ResponseEntity<CategoryResponse>
    updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryRequest request
    ) {

        return ResponseEntity.ok(
                categoryService.update(
                        id,
                        request
                )
        );
    }

    // =====================================================
    // ADMIN - DELETE
    // =====================================================

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void>
    deleteCategory(
            @PathVariable Long id
    ) {

        categoryService.delete(id);

        return ResponseEntity.noContent().build();
    }
}