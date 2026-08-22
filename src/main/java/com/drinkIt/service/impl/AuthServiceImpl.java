package com.drinkIt.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.drinkIt.dto.auth.AuthResponse;
import com.drinkIt.dto.auth.LoginRequest;
import com.drinkIt.dto.auth.RegisterRequest;
import com.drinkIt.entity.User;
import com.drinkIt.enums.Role;
import com.drinkIt.enums.UserStatus;
import com.drinkIt.repository.UserRepository;
import com.drinkIt.security.JwtService;
import com.drinkIt.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @Override
    public AuthResponse register(
            RegisterRequest request
    ) {

         String email = request.getEmail().trim().toLowerCase();

        String phone =  request.getPhone().trim();

        if (userRepository.existsByEmail(
                request.getEmail()
        )) {

            throw new RuntimeException(
                    "Email already registered"
            );
        }

        if (userRepository.existsByPhone(
                request.getPhone()
        )) {

            throw new RuntimeException(
                    "Phone already registered"
            );
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(
                        passwordEncoder.encode(request.getPassword())
                          )
                .role(Role.CUSTOMER)
                .status(UserStatus.ACTIVE)
                .build();

        User savedUser =
                userRepository.save(user);

        return createAuthResponse(savedUser);
    }

    @Override
    public AuthResponse login(
            LoginRequest request
    ) {

        authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(

                        request.getEmail(),

                        request.getPassword()
                )
        );

        User user =
                userRepository
                        .findByEmail(
                                request.getEmail()
                        )
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "User not found"
                                )
                        );

        if (user.getStatus()
                != UserStatus.ACTIVE) {

            throw new RuntimeException(
                    "Account is not active"
            );
        }

        /*
         * The current role is read from DB.
         *
         * CUSTOMER
         * VENDOR
         * DELIVERY_PARTNER
         * ADMIN
         */

        return createAuthResponse(user);
    }

    private AuthResponse createAuthResponse(
            User user
    ) {

        String token =
                jwtService.generateToken(
                        user.getEmail()
                );

        return AuthResponse.builder()

                .token(token)

                .userId(user.getId())

                .name(user.getName())

                .email(user.getEmail())

                .role(user.getRole())

                .build();
    }
}