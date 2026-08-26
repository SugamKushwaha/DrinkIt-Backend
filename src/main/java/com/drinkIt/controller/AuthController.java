package com.drinkIt.controller;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.drinkIt.dto.auth.AuthResponse;
import com.drinkIt.dto.auth.LoginRequest;
import com.drinkIt.dto.auth.RegisterRequest;
import com.drinkIt.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request,
            HttpServletResponse response
    ) {

        AuthResponse authResponse = authService.register(request);

        setAuthCookie(response, authResponse.getToken());

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {

        AuthResponse authResponse = authService.login(request);

        setAuthCookie(response, authResponse.getToken());

        return ResponseEntity.ok(authResponse);
    }

    // =====================================================
    // COOKIE HELPER
    //
    // secure(false) + sameSite("Lax") work for local dev
    // (http://localhost). In production over HTTPS with the
    // frontend on a different domain, use secure(true) and
    // sameSite("None").
    // =====================================================

    private void setAuthCookie(HttpServletResponse response, String token) {

        ResponseCookie cookie =
                ResponseCookie.from("drinkit-token", token)
                        .httpOnly(true)
                        .secure(false)
                        .path("/")
                        .maxAge(24 * 60 * 60)
                        .sameSite("Lax")
                        .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
        );
    }

    @PostMapping("/logout")
public ResponseEntity<?> logout( HttpServletResponse response){
 
    ResponseCookie cookie =
            ResponseCookie.from("drinkit-token", "")
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(0)
                    .sameSite("Lax")
                    .build();

    response.addHeader(
            HttpHeaders.SET_COOKIE,
            cookie.toString()
    );

    return ResponseEntity.ok(
            Map.of(
                    "message",
                    "Logout successful"
            )
    );
}
            
}