package com.drinkIt.dto.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.drinkIt.enums.ProductStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private Long id;

    private String name;

    private String brand;

    private String category;

    private String volume;

    private BigDecimal price;

    private BigDecimal oldPrice;

    private String description;

    private String image;

    private Integer stock;

    private Boolean popular;

    private ProductStatus status;

    private Long vendorId;

    private String vendorName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}