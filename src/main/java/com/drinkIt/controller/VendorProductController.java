package com.drinkIt.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
import com.drinkIt.entity.User;
import com.drinkIt.security.CurrentUserService;
import com.drinkIt.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/vendor/products")
@RequiredArgsConstructor
@PreAuthorize("hasRole('VENDOR')")
public class VendorProductController {

    private final ProductService productService;

    private final CurrentUserService currentUserService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @RequestBody ProductRequest request,
            Authentication authentication
    ) {

        User user =
                currentUserService.getUser(authentication);

        return ResponseEntity.ok(
                productService.createByVendor(
                        request,
                        user.getId()
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts(
            Authentication authentication
    ) {

        User user =
                currentUserService.getUser(authentication);

        return ResponseEntity.ok(
                productService.getVendorProducts(
                        user.getId()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(
            @PathVariable Long id,
            Authentication authentication
    ) {

        User user =
                currentUserService.getUser(authentication);

        return ResponseEntity.ok(
                productService.getVendorProduct(
                        id,
                        user.getId()
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequest request,
            Authentication authentication
    ) {

        User user =
                currentUserService.getUser(authentication);

        return ResponseEntity.ok(
                productService.updateByVendor(
                        id,
                        request,
                        user.getId()
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id,
            Authentication authentication
    ) {

        User user =
                currentUserService.getUser(authentication);

        productService.deleteByVendor(
                id,
                user.getId()
        );

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<ProductResponse> toggleStatus(
            @PathVariable Long id,
            Authentication authentication
    ) {

        User user =
                currentUserService.getUser(authentication);

        return ResponseEntity.ok(
                productService.toggleStatusByVendor(
                        id,
                        user.getId()
                )
        );
    }
}