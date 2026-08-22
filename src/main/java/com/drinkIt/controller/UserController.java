package com.drinkIt.controller;

import com.drinkIt.dto.user.UserResponse;
import com.drinkIt.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

   @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(
            Authentication authentication
    ) {

        if (authentication == null
                || !authentication.isAuthenticated()) {

            return ResponseEntity
                    .status(401)
                    .build();
        }

        String email =
                authentication.getName();

        UserResponse user =
                userService.getCurrentUser(email);

        return ResponseEntity.ok(user);
    }
}