package com.drinkIt.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<List<CategoryResponse>> getActiveCategories() {

        return ResponseEntity.ok(
                categoryService.getActive()
        );
    }

    // =====================================================
    // ADMIN - GET ALL
    // =====================================================

    @GetMapping("/admin")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {

        return ResponseEntity.ok(
                categoryService.getAll()
        );
    }

    // =====================================================
    // ADMIN - GET ONE
    // =====================================================

    @GetMapping("/admin/{id}")
    public ResponseEntity<CategoryResponse> getCategory(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                categoryService.getById(id)
        );
    }

    // =====================================================
    // ADMIN - CREATE
    // =====================================================

    @PostMapping(
            value = "/admin",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<CategoryResponse> createCategory(

            @RequestParam("name")
            String name,

            @RequestParam("image")
            MultipartFile image,

            @RequestPart(value = "active", required = false)
            Boolean active

    ) {

        return ResponseEntity.ok(
                categoryService.create(
                        name,
                        image,
                        active
                )
        );
    }

    // =====================================================
    // ADMIN - UPDATE
    // =====================================================

    @PutMapping(
            value = "/admin/{id}",
            consumes = "multipart/form-data"
    )
    public ResponseEntity<CategoryResponse> updateCategory(

            @PathVariable Long id,

            @RequestParam("name")
            String name,

            @RequestParam(value = "image", required = false)
            MultipartFile image,

            @RequestParam(value = "active", required = false)
            Boolean active

    ) {

        return ResponseEntity.ok(
                categoryService.update(
                        id,
                        name,
                        image,
                        active
                )
        );
    }

    // =====================================================
    // ADMIN - DELETE
    // =====================================================

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long id
    ) {

        categoryService.delete(id);

        return ResponseEntity.noContent().build();
    }
}