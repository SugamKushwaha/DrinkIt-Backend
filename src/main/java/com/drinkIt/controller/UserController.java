package com.drinkIt.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.drinkIt.dto.user.UserAddressResponse;
import com.drinkIt.dto.user.UserAddressSaveRequest;
import com.drinkIt.dto.user.UserAddressUpdateRequest;
import com.drinkIt.dto.user.UserResponse;
import com.drinkIt.dto.user.UserUpdateRequest;
import com.drinkIt.service.UserService;

import lombok.RequiredArgsConstructor;

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

    @GetMapping("/addresses")
public ResponseEntity<List<UserAddressResponse>> getAddresses(
        Authentication authentication
) {

    List<UserAddressResponse> addresses =
            userService.getAddresses(authentication);

    return ResponseEntity.ok(addresses);
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
