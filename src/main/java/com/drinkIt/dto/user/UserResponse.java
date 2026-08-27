package com.drinkIt.dto.user;


import java.time.LocalDateTime;

import com.drinkIt.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long id;

    private String name;

    private String email;

    private String phone;

    private Role role;

     private boolean verified;

    private LocalDateTime createdAt;

}