package com.drinkIt.service;

import com.drinkIt.dto.user.UserResponse;

public interface  UserService {

    public UserResponse getCurrentUser( String email); 
    
}
