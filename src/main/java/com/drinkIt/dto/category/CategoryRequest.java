package com.drinkIt.dto.category;

import lombok.Data;

@Data
public class CategoryRequest {

    private String name;

    private String image;

    private Boolean active;
}