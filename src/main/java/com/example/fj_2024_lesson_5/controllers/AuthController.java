package com.example.fj_2024_lesson_5.controllers;

import com.example.fj_2024_lesson_5.dto.JwtAuthenticationResponse;
import com.example.fj_2024_lesson_5.dto.PasswordResetRequest;
import com.example.fj_2024_lesson_5.dto.SignInRequest;
import com.example.fj_2024_lesson_5.dto.SignUpRequest;
import com.example.fj_2024_lesson_5.services.AuthService;
import com.example.fj_2024_lesson_5.services.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Log4j2
public class AuthController {
    private final AuthService authService;
    private final LogoutService logoutService;

    @PostMapping("/sign-up")
    public ResponseEntity<JwtAuthenticationResponse> signUp(@RequestBody @Valid SignUpRequest request) {
        JwtAuthenticationResponse response = authService.signUp(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody @Valid SignInRequest request) {
        JwtAuthenticationResponse response = authService.signIn(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            log.info(token);
            logoutService.invalidateToken(token);
        }

        return ResponseEntity.ok("You have successfully logged out");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<JwtAuthenticationResponse> resetPassword(@RequestBody @Valid PasswordResetRequest request, HttpServletRequest httpRequest) {
        String authHeader = httpRequest.getHeader("Authorization");
        JwtAuthenticationResponse response = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            log.info(token);
            response = authService.resetPassword(request);
            logoutService.invalidateToken(token);
        }
        return ResponseEntity.ok(response);
    }
}