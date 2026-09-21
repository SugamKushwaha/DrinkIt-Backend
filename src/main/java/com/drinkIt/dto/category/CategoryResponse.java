package com.drinkIt.dto.category;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponse {

    private Long id;

    /*
     * Admin frontend uses name.
     */
    private String name;

    /*
     * Customer CategoryCard uses title.
     */
    private String title;

    private String image;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}