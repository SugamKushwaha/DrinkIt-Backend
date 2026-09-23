package com.drinkIt.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.drinkIt.dto.product.ProductImageResponse;
import com.drinkIt.dto.product.ProductResponse;
import com.drinkIt.service.ProductImageService;
import com.drinkIt.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {


    private final ProductService productService;

    private final ProductImageService productImageService;


    // =====================================================
    // ALL ACTIVE PRODUCTS
    // =====================================================

    @GetMapping
    public ResponseEntity<List<ProductResponse>>
    getProducts() {

        return ResponseEntity.ok(
                productService.getActiveProducts()
        );
    }


    // =====================================================
    // PRODUCT DETAILS
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse>
    getProduct(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                productService.getActiveProduct(id)
        );
    }


    // =====================================================
    // POPULAR TONIGHT
    // =====================================================

    @GetMapping("/popular")
    public ResponseEntity<List<ProductResponse>>
    getPopularProducts() {

        return ResponseEntity.ok(
                productService.getPopularProducts()
        );
    }

    // =====================================================
    // CATEGORY
    // =====================================================

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponse>>
    getByCategory(
            @PathVariable String category
    ) {

        return ResponseEntity.ok(
                productService
                        .getActiveProductsByCategory(
                                category
                        )
        );
    }

    @GetMapping("/image")
public ResponseEntity<ProductImageResponse>
getProductImage(
        @RequestParam String name
) {

    return ResponseEntity.ok(
            productImageService
                    .getImageByProductName(name)
    );
}
}