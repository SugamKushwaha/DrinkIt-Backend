package com.drinkIt.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.drinkIt.dto.product.ProductImageResponse;

public interface ProductImageService {

    ProductImageResponse uploadImage(
            String productName,
            MultipartFile image
    );

    ProductImageResponse getImageByProductName(
            String productName
    );

    List<ProductImageResponse> getAllImages();

    void deleteImage(Long id);
}