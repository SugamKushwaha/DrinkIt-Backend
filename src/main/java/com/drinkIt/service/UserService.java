package com.drinkIt.service;

import org.springframework.security.core.Authentication;

import com.drinkIt.dto.user.UserResponse;
import com.drinkIt.dto.user.UserUpdateRequest;
import com.drinkIt.dto.user.UserAddressResponse;
import com.drinkIt.dto.user.UserAddressSaveRequest;
import com.drinkIt.dto.user.UserAddressUpdateRequest;

public interface  UserService {

    UserResponse getCurrentUser( String email);
    
    UserResponse updateUser(String email, UserUpdateRequest request);

    UserAddressResponse saveAddress( UserAddressSaveRequest request, Authentication authentication);

    UserAddressResponse updateAddress(Long addressId, UserAddressUpdateRequest request, Authentication authentication);

    void deleteAddress(Long addressId, Authentication authentication);

    

}
