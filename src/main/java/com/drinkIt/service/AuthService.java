package com.drinkIt.service;

import com.drinkIt.dto.auth.AuthResponse;
import com.drinkIt.dto.auth.LoginRequest;
import com.drinkIt.dto.auth.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}