package com.drinkIt.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.drinkIt.entity.Product;
import com.drinkIt.entity.Vendor;
import com.drinkIt.enums.ProductStatus;

public interface ProductRepository
        extends JpaRepository<Product, Long> {


    // =====================================================
    // PUBLIC PRODUCTS
    // =====================================================

    List<Product> findByStatus(ProductStatus status);


    // =====================================================
    // VENDOR PRODUCTS
    // =====================================================

    List<Product> findByVendor(Vendor vendor);


    // =====================================================
    // VENDOR + STATUS
    // =====================================================

    List<Product> findByVendorAndStatus(
            Vendor vendor,
            ProductStatus status
    );


    // =====================================================
    // CATEGORY
    // =====================================================

    List<Product> findByCategoryIgnoreCase(
            String category
    );


    // =====================================================
    // PUBLIC CATEGORY
    // =====================================================

    List<Product> findByCategoryIgnoreCaseAndStatus(
            String category,
            ProductStatus status
    );
}