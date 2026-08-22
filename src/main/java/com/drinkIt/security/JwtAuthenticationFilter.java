package com.drinkIt.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authorizationHeader =
                request.getHeader("Authorization");

        String token = null;
        String email = null;

        // =====================================================
        // 1. CHECK AUTHORIZATION HEADER
        // =====================================================

        if (authorizationHeader != null
                && authorizationHeader.startsWith("Bearer ")) {

            token = authorizationHeader.substring(7);

            try {

                email = jwtService.extractEmail(token);

            } catch (Exception e) {

                System.out.println(
                        "JWT extraction failed: "
                                + e.getMessage()
                );
            }
        }

        // =====================================================
        // 2. AUTHENTICATE USER
        // =====================================================

        if (email != null
                && SecurityContextHolder
                .getContext()
                .getAuthentication() == null) {

            try {

                UserDetails userDetails =
                        userDetailsService
                                .loadUserByUsername(email);

                // =================================================
                // 3. VALIDATE JWT
                // =================================================

                if (jwtService.isTokenValid(
                        token,
                        userDetails.getUsername()
                )) {

                    UsernamePasswordAuthenticationToken
                            authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);

                    System.out.println(
                            "JWT Authentication successful: "
                                    + email
                    );
                }

            } catch (Exception e) {

                System.out.println(
                        "JWT Authentication failed: "
                                + e.getMessage()
                );
            }
        }

        // =====================================================
        // 4. CONTINUE FILTER CHAIN
        // =====================================================

        filterChain.doFilter(
                request,
                response
        );
    }
}