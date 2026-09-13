package com.drinkIt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.drinkIt.entity.ProductImage;

public interface ProductImageRepository
        extends JpaRepository<ProductImage, Long> {

    Optional<ProductImage> findByNormalizedName(
            String normalizedName
    );

    boolean existsByNormalizedName(
            String normalizedName
    );
}