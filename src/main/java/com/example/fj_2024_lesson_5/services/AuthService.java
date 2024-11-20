package com.example.fj_2024_lesson_5.services;

import com.example.fj_2024_lesson_5.dto.JwtAuthenticationResponse;
import com.example.fj_2024_lesson_5.dto.PasswordResetRequest;
import com.example.fj_2024_lesson_5.dto.SignInRequest;
import com.example.fj_2024_lesson_5.dto.SignUpRequest;
import com.example.fj_2024_lesson_5.entity.Role;
import com.example.fj_2024_lesson_5.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userService.create(user);

        return new JwtAuthenticationResponse(jwtService.generateToken(user, false));
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));

        var user = userService.userDetailsService().loadUserByUsername(request.getUsername());

        return new JwtAuthenticationResponse(jwtService.generateToken(user, request.isRememberedUser()));
    }

    public JwtAuthenticationResponse resetPassword(PasswordResetRequest request) {
        if (!"0000".equals(request.getConfirmationCode())) {
            throw new IllegalArgumentException("Неверный код подтверждения");
        }

        var user = userService.getByUsername(request.getUsername());

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userService.update(user);

        UserDetails userDetails = userService.getByUsername(request.getUsername());

        return new JwtAuthenticationResponse(jwtService.generateToken(userDetails, true));
    }
}
