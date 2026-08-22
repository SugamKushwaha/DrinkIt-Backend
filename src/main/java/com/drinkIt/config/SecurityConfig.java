package com.drinkIt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.drinkIt.security.CustomUserDetailsService;
import com.drinkIt.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final CustomUserDetailsService userDetailsService;

    // =====================================================
    // PASSWORD ENCODER
    // =====================================================

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    // =====================================================
    // AUTHENTICATION PROVIDER
    // =====================================================

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(
                        userDetailsService
                );

        provider.setPasswordEncoder(
                passwordEncoder()
        );

        return provider;
    }

    // =====================================================
    // AUTHENTICATION MANAGER
    // =====================================================

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration
    ) throws Exception {

        return configuration
                .getAuthenticationManager();
    }

    // =====================================================
    // SECURITY FILTER CHAIN
    // =====================================================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

                // -------------------------------------------------
                // CSRF
                // -------------------------------------------------

                .csrf(csrf -> csrf.disable())

                // -------------------------------------------------
                // STATELESS JWT
                // -------------------------------------------------

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // -------------------------------------------------
                // AUTHORIZATION
                // -------------------------------------------------

                .authorizeHttpRequests(auth -> auth

                        // Authentication APIs
                        .requestMatchers(
                                "/api/auth/**"
                        ).permitAll()

                        // Current logged-in user
                        .requestMatchers(
                                "/api/users/me"
                        ).authenticated()

                        // Products
                        .requestMatchers(
                                "/api/products/**"
                        ).permitAll()

                        // Admin
                        .requestMatchers(
                                "/api/admin/**"
                        ).hasRole("ADMIN")

                        // Vendor
                        .requestMatchers(
                                "/api/vendor/**"
                        ).hasRole("VENDOR")

                        // Delivery
                        .requestMatchers(
                                "/api/delivery/**"
                        ).hasRole("DELIVERY_PARTNER")

                        // Customer
                        .requestMatchers(
                                "/api/customer/**"
                        ).hasRole("CUSTOMER")

                        // Vendor requests
                        .requestMatchers(
                                "/api/vendor-requests/**"
                        ).authenticated()

                        // Delivery requests
                        .requestMatchers(
                                "/api/delivery-partner-requests/**"
                        ).authenticated()

                        // Everything else
                        .anyRequest()
                        .authenticated()
                )

                // -------------------------------------------------
                // AUTH PROVIDER
                // -------------------------------------------------

                .authenticationProvider(
                        authenticationProvider()
                )

                // -------------------------------------------------
                // JWT FILTER
                // -------------------------------------------------

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}