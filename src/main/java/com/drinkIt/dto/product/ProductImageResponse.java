package com.drinkIt.dto.product;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageResponse {

    private Long id;

    private String productName;

    private String imageUrl;

    private String fileName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}