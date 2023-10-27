package com.andrewlalis.onyx.auth.api;

import com.andrewlalis.onyx.auth.model.User;
import com.andrewlalis.onyx.auth.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final TokenService tokenService;

    @PostMapping("/auth/login")
    public TokenPair login(@RequestBody LoginRequest loginRequest) {
        return tokenService.generateTokenPair(loginRequest);
    }

    @GetMapping("/auth/access")
    public AccessTokenResponse getAccessToken(HttpServletRequest request) {
        return tokenService.generateAccessToken(request);
    }

    @DeleteMapping("/auth/refresh-tokens")
    public void removeAllRefreshTokens(@AuthenticationPrincipal User user) {
        tokenService.removeAllRefreshTokens(user);
    }

    @GetMapping("/auth/token-expiration")
    public Object getTokenExpiration(HttpServletRequest request) {
        return Map.of("expiresAt", tokenService.getTokenExpiration(request));
    }
}
