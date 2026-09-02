package com.zest.product_api.controller;

import com.zest.product_api.dto.request.LoginRequest;
import com.zest.product_api.dto.request.RefreshTokenRequest;
import com.zest.product_api.dto.response.LoginResponse;
import com.zest.product_api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {

        LoginResponse response = authService.login(
                request.getUsername(),
                request.getPassword()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(
            @Valid @RequestBody RefreshTokenRequest request
    ) {

        LoginResponse response = authService.refresh(
                request.getRefreshToken()
        );

        return ResponseEntity.ok(response);
    }
}