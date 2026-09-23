package com.drinkIt.service;

import java.util.List;

import com.drinkIt.dto.product.ProductRequest;
import com.drinkIt.dto.product.ProductResponse;

public interface ProductService {


    // =====================================================
    // ADMIN
    // =====================================================

    ProductResponse createByAdmin(ProductRequest request);

    List<ProductResponse> getAllForAdmin();

    ProductResponse getAdminProduct(Long id);

    ProductResponse updateByAdmin(
            Long id,
            ProductRequest request
    );

    void deleteByAdmin(Long id);

    ProductResponse toggleStatusByAdmin(Long id);


    // =====================================================
    // VENDOR
    // =====================================================

    ProductResponse createByVendor(
            ProductRequest request,
            Long userId
    );

    List<ProductResponse> getVendorProducts(
            Long userId
    );

    ProductResponse getVendorProduct(
            Long id,
            Long userId
    );

    ProductResponse updateByVendor(
            Long id,
            ProductRequest request,
            Long userId
    );

    void deleteByVendor(
            Long id,
            Long userId
    );

    ProductResponse toggleStatusByVendor(
            Long id,
            Long userId
    );


    // =====================================================
    // CUSTOMER
    // =====================================================

    List<ProductResponse> getActiveProducts();

    ProductResponse getActiveProduct(Long id);

    List<ProductResponse> getActiveProductsByCategory(
            String category
    );

    List<ProductResponse> getPopularProducts();
}