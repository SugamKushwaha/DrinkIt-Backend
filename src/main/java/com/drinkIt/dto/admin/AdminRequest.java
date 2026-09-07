package com.drinkIt.dto.admin;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminRequest {

    private String name;

    private String email;

    private String phone;

    private String password;

    private String role;

}