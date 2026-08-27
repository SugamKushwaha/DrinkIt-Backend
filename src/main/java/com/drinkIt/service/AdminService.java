package com.drinkIt.service;

import java.util.List;

import com.drinkIt.dto.user.UserResponse;

public interface AdminService {
    
    List<UserResponse> allCustomers();

    List<UserResponse> allVendors();
}
