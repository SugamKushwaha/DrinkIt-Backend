package com.drinkIt.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.drinkIt.dto.category.CategoryResponse;
import com.drinkIt.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminCategoryController {

    private final CategoryService categoryService;


    // =====================================================
    // UPLOAD / ADD CATEGORY
    // =====================================================

    @PostMapping(
            value = "/upload",
            consumes =
                    MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<CategoryResponse>
    uploadCategory(

            @RequestParam("categoryName")
            String categoryName,

            @RequestParam("image")
            MultipartFile image
    ) {

        return ResponseEntity.ok(
                categoryService.uploadImage(
                        categoryName,
                        image
                )
        );
    }


    // =====================================================
    // GET ALL
    // =====================================================

    @GetMapping
    public ResponseEntity<List<CategoryResponse>>
    getAllCategories() {

        return ResponseEntity.ok(
                categoryService.getAllImages()
        );
    }


    // =====================================================
    // GET BY NAME
    // =====================================================

    @GetMapping("/by-name")
    public ResponseEntity<CategoryResponse>
    getCategory(

            @RequestParam("name")
            String name
    ) {

        return ResponseEntity.ok(
                categoryService
                        .getImageByCategoryName(name)
        );
    }


    // =====================================================
    // DELETE
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deleteCategory(

            @PathVariable Long id
    ) {

        categoryService.deleteImage(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}