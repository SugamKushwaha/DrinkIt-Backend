package com.drinkIt.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.drinkIt.dto.product.ProductRequest;
import com.drinkIt.dto.product.ProductResponse;
import com.drinkIt.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminProductController {


    private final ProductService productService;


    // =====================================================
    // CREATE
    // =====================================================

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @RequestBody ProductRequest request
    ) {

        return ResponseEntity.ok(
                productService.createByAdmin(request)
        );
    }


    // =====================================================
    // GET ALL
    // =====================================================

    @GetMapping
    public ResponseEntity<List<ProductResponse>>
    getProducts() {

        return ResponseEntity.ok(
                productService.getAllForAdmin()
        );
    }


    // =====================================================
    // GET ONE
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse>
    getProduct(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                productService.getAdminProduct(id)
        );
    }


    // =====================================================
    // UPDATE
    // =====================================================

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse>
    updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequest request
    ) {

        return ResponseEntity.ok(
                productService.updateByAdmin(
                        id,
                        request
                )
        );
    }


    // =====================================================
    // DELETE
    // =====================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deleteProduct(
            @PathVariable Long id
    ) {

        productService.deleteByAdmin(id);

        return ResponseEntity.noContent()
                .build();
    }


    // =====================================================
    // TOGGLE STATUS
    // =====================================================

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<ProductResponse>
    toggleStatus(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                productService.toggleStatusByAdmin(id)
        );
    }
}