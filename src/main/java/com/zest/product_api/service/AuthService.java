package com.zest.product_api.service;

import com.zest.product_api.dto.response.LoginResponse;
import com.zest.product_api.entity.RefreshToken;
import com.zest.product_api.entity.User;
import com.zest.product_api.repository.RefreshTokenRepository;
import com.zest.product_api.repository.UserRepository;
import com.zest.product_api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse login(String username, String password) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("Invalid username or password")
                );

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        String accessToken = jwtService.generateAccessToken(
                user.getUsername(),
                user.getRole()
        );

        String refreshToken = jwtService.generateRefreshToken(
                user.getUsername()
        );

        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setUsername(user.getUsername());
        refreshTokenEntity.setExpiryDate(
                LocalDateTime.now().plusDays(7)
        );
        refreshTokenEntity.setRevoked(false);

        refreshTokenRepository.save(refreshTokenEntity);

        return new LoginResponse(accessToken, refreshToken);
    }

    public LoginResponse refresh(String token) {

        RefreshToken storedToken = refreshTokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new RuntimeException("Invalid refresh token")
                );

        if (storedToken.isRevoked()) {
            throw new RuntimeException("Refresh token has been revoked");
        }

        if (storedToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh token has expired");
        }

        if (!jwtService.isTokenValid(token) ||
                !jwtService.isRefreshToken(token)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String username = jwtService.extractUsername(token);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

        // Revoke old refresh token
        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);

        // Generate new tokens
        String newAccessToken = jwtService.generateAccessToken(
                user.getUsername(),
                user.getRole()
        );

        String newRefreshToken = jwtService.generateRefreshToken(
                user.getUsername()
        );

        RefreshToken newTokenEntity = new RefreshToken();
        newTokenEntity.setToken(newRefreshToken);
        newTokenEntity.setUsername(user.getUsername());
        newTokenEntity.setExpiryDate(
                LocalDateTime.now().plusDays(7)
        );
        newTokenEntity.setRevoked(false);

        refreshTokenRepository.save(newTokenEntity);

        return new LoginResponse(
                newAccessToken,
                newRefreshToken
        );
    }
}