package com.drinkIt.dto.category;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponse {

    private Long id;

    private String name;

    private String image;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}