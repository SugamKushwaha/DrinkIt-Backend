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

import com.drinkIt.dto.product.ProductImageResponse;
import com.drinkIt.service.ProductImageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/product-images")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductImageController {

    private final ProductImageService productImageService;


    // =====================================================
    // UPLOAD / REPLACE IMAGE
    // =====================================================

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ProductImageResponse>
    uploadImage(
            @RequestParam("productName")
            String productName,

            @RequestParam("image")
            MultipartFile image
    ) {

        return ResponseEntity.ok(
                productImageService.uploadImage(
                        productName,
                        image
                )
        );
    }


    // =====================================================
    // GET ALL
    // =====================================================

    @GetMapping
    public ResponseEntity<List<ProductImageResponse>>
    getAllImages() {

        return ResponseEntity.ok(
                productImageService.getAllImages()
        );
    }


    // =====================================================
    // GET BY NAME
    // =====================================================

    @GetMapping("/by-name")
    public ResponseEntity<ProductImageResponse>
    getImage(
            @RequestParam String name
    ) {

        return ResponseEntity.ok(
                productImageService
                        .getImageByProductName(
                                name
                        )
        );
    }


    // =====================================================
    // DELETE
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deleteImage(
            @PathVariable Long id
    ) {

        productImageService.deleteImage(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    
}