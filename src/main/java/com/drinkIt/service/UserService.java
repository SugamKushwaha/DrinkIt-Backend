package com.drinkIt.service;

import com.drinkIt.dto.user.UserResponse;
import com.drinkIt.dto.user.UserUpdateRequest;

public interface  UserService {

    public UserResponse getCurrentUser( String email);
    
    UserResponse updateUser(String email, UserUpdateRequest request);


    
}
