package com.drinkIt.dto.admin;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminResponse {

    private Long id;

    private Long userId;

    private String name;

    private String email;

    private String phone;

    private String role;

    private String status;

    private LocalDateTime createdAt;

}