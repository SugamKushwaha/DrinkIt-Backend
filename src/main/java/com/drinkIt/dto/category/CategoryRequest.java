package com.drinkIt.dto.category;

import lombok.Data;

@Data
public class CategoryRequest {

    private String name;

    private Boolean active;
}