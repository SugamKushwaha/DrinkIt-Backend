package com.drinkIt.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.drinkIt.entity.User;
import com.drinkIt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;

    public User getUser(
            Authentication authentication
    ) {

        String email =
                authentication.getName();

        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Authenticated user not found"
                        )
                );
    }

    public Long getUserId(
            Authentication authentication
    ) {

        return getUser(authentication)
                .getId();
    }
}