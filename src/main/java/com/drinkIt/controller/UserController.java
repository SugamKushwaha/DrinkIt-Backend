package com.drinkIt.controller;

import com.drinkIt.dto.user.UserAddressSaveRequest;
import com.drinkIt.dto.user.UserAddressUpdateRequest;
import com.drinkIt.dto.user.UserResponse;
import com.drinkIt.dto.user.UserUpdateRequest;
import com.drinkIt.service.UserService;
import com.drinkIt.dto.user.UserAddressResponse;


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
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication ){

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity
                    .status(401)
                    .build();
        }
        String email = authentication.getName();
        UserResponse user = userService.getCurrentUser(email);

        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    public UserResponse updateCurrentUser( Authentication authentication, @RequestBody UserUpdateRequest request) {

        return userService.updateUser(
                authentication.getName(),
                request
        );
  }

    @PostMapping("/addresses")
    public ResponseEntity<UserAddressResponse> saveAddress( @RequestBody UserAddressSaveRequest request, Authentication authentication) {

        UserAddressResponse response =
                userService.saveAddress(
                        request,
                        authentication
                );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<UserAddressResponse> updateAddress( @PathVariable Long addressId, @RequestBody UserAddressUpdateRequest request, Authentication authentication) {

        UserAddressResponse response =
                userService.updateAddress(
                        addressId,
                        request,
                        authentication
                );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/addresses/{addressId}")
     public ResponseEntity<Void> deleteAddress( @PathVariable Long addressId, Authentication authentication) {

         userService.deleteAddress(
                 addressId,
                 authentication
         );

         return ResponseEntity.noContent().build();
     }
}
