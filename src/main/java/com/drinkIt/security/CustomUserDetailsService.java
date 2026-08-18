package com.drinkIt.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.drinkIt.entity.User;
import com.drinkIt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(
            String email
    ) throws UsernameNotFoundException {

        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "User not found"
                                )
                        );

        return new org.springframework.security
                .core.userdetails.User(

                user.getEmail(),

                user.getPassword(),

                user.getStatus()
                        .name()
                        .equals("ACTIVE"),

                true,

                true,

                true,

                List.of(
                        new SimpleGrantedAuthority(
                                "ROLE_" +
                                user.getRole().name()
                        )
                )
        );
    }
}