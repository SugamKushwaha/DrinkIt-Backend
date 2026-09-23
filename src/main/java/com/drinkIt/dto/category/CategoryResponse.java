package com.drinkIt.dto.category;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {

    private Long id;

    private String categoryName;

    private String imageUrl;

    private String fileName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}