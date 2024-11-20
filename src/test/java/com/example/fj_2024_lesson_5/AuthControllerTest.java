package com.example.fj_2024_lesson_5;

import com.example.fj_2024_lesson_5.controllers.AuthController;
import com.example.fj_2024_lesson_5.dto.JwtAuthenticationResponse;
import com.example.fj_2024_lesson_5.dto.PasswordResetRequest;
import com.example.fj_2024_lesson_5.dto.SignInRequest;
import com.example.fj_2024_lesson_5.dto.SignUpRequest;
import com.example.fj_2024_lesson_5.services.AuthService;
import com.example.fj_2024_lesson_5.services.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private LogoutService logoutService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signUp_ValidRequest_ShouldReturnJwtAuthenticationResponse() {
        SignUpRequest request = new SignUpRequest("user", "email@example.com", "password");
        JwtAuthenticationResponse expectedResponse = new JwtAuthenticationResponse("token");
        when(authService.signUp(any(SignUpRequest.class))).thenReturn(expectedResponse);

        ResponseEntity<JwtAuthenticationResponse> actualResponse = authController.signUp(request);

        assertNotNull(actualResponse);
        assertEquals(HttpStatus.CREATED, actualResponse.getStatusCode()); // Проверка статуса 201 Created
        assertNotNull(actualResponse.getBody());
        assertEquals(expectedResponse, actualResponse.getBody());
        verify(authService, times(1)).signUp(request);
    }

    @Test
    void signUp_InvalidRequest_ShouldThrowException() {
        SignUpRequest invalidRequest = new SignUpRequest("", "email", "");
        when(authService.signUp(any(SignUpRequest.class)))
                .thenThrow(new IllegalArgumentException("Invalid data"));

        assertThrows(IllegalArgumentException.class, () -> authController.signUp(invalidRequest));
        verify(authService, times(1)).signUp(invalidRequest);
    }

    @Test
    void signIn_ValidRequest_ShouldReturnJwtAuthenticationResponse() {
        SignInRequest request = new SignInRequest("user", "password", true);
        JwtAuthenticationResponse expectedResponse = new JwtAuthenticationResponse("token");
        when(authService.signIn(any(SignInRequest.class))).thenReturn(expectedResponse);

        ResponseEntity<JwtAuthenticationResponse> actualResponse = authController.signIn(request);

        assertNotNull(actualResponse);
        assertEquals(HttpStatus.OK, actualResponse.getStatusCode());
        assertNotNull(actualResponse.getBody());
        assertEquals(expectedResponse, actualResponse.getBody());
        verify(authService, times(1)).signIn(request);
    }

    @Test
    void signIn_InvalidCredentials_ShouldThrowException() {
        SignInRequest invalidRequest = new SignInRequest("user", "wrongPassword", false);
        when(authService.signIn(any(SignInRequest.class)))
                .thenThrow(new IllegalArgumentException("Invalid credentials"));

        assertThrows(IllegalArgumentException.class, () -> authController.signIn(invalidRequest));
        verify(authService, times(1)).signIn(invalidRequest);
    }

    @Test
    void logout_WithBearerToken_ShouldInvalidateTokenAndReturnSuccessMessage() {

        String token = "token";
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer " + token);

        ResponseEntity<?> response = authController.logout(httpServletRequest);

        assertEquals(ResponseEntity.ok("You have successfully logged out"), response);
        verify(logoutService, times(1)).invalidateToken(token);
    }

    @Test
    void resetPassword_WithValidToken_ShouldResetPasswordAndReturnJwtAuthenticationResponse() {
        PasswordResetRequest request = new PasswordResetRequest("user", "0000", "newPassword");
        JwtAuthenticationResponse expectedResponse = new JwtAuthenticationResponse("token");
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer token");
        when(authService.resetPassword(request)).thenReturn(expectedResponse);

        ResponseEntity<JwtAuthenticationResponse> response = authController.resetPassword(request, httpServletRequest);

        assertEquals(ResponseEntity.ok(expectedResponse), response);
        verify(authService, times(1)).resetPassword(request);
        verify(logoutService, times(1)).invalidateToken("token");
    }

    @Test
    void resetPassword_InvalidConfirmationCode_ShouldThrowException() {
        PasswordResetRequest request = new PasswordResetRequest("user", "wrongCode", "newPassword");
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer token");
        when(authService.resetPassword(request)).thenThrow(new IllegalArgumentException("Invalid confirmation code"));

        assertThrows(IllegalArgumentException.class, () -> authController.resetPassword(request, httpServletRequest));
        verify(authService, times(1)).resetPassword(request);
    }

    @Test
    void resetPassword_WithInvalidToken_ShouldReturnNullResponse() {
        PasswordResetRequest request = new PasswordResetRequest("user", "0000", "newPassword");
        when(httpServletRequest.getHeader("Authorization")).thenReturn(null);

        ResponseEntity<JwtAuthenticationResponse> response = authController.resetPassword(request, httpServletRequest);
        assertEquals(ResponseEntity.ok(null), response);
        verify(authService, never()).resetPassword(request);
        verify(logoutService, never()).invalidateToken(anyString());
    }
}