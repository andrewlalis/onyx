package com.andrewlalis.onyx.auth.api;

import com.andrewlalis.onyx.auth.model.User;
import com.andrewlalis.onyx.auth.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller for authentication-related tasks.
 */
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final TokenService tokenService;

    /**
     * The endpoint for users of this Onyx node to login, that is, obtain a
     * refresh token and access token in exchange for valid credentials.
     * @param loginRequest The login request.
     * @return A token pair, if successful.
     */
    @PostMapping("/auth/login")
    public TokenPair login(@RequestBody LoginRequest loginRequest) {
        return tokenService.generateTokenPair(loginRequest);
    }

    /**
     * Endpoint for obtaining a new access token using a valid refresh token.
     * @param request The HTTP request.
     * @return The new access token.
     */
    @GetMapping("/auth/access")
    public AccessTokenResponse getAccessToken(HttpServletRequest request) {
        return tokenService.generateAccessToken(request);
    }

    /**
     * Endpoint used to remove all refresh tokens, essentially logging the user
     * out of all devices that may have stored a refresh token.
     * @param user The user who is removing their tokens.
     */
    @DeleteMapping("/auth/refresh-tokens")
    public void removeAllRefreshTokens(@AuthenticationPrincipal User user) {
        tokenService.removeAllRefreshTokens(user);
    }

    /**
     * Endpoint for determining the expiration time of an access token.
     * @param request The HTTP request.
     * @return An object containing an "expiresAt" field, in milliseconds since
     * the unix epoch.
     */
    @GetMapping("/auth/token-expiration")
    public Object getTokenExpiration(HttpServletRequest request) {
        return Map.of("expiresAt", tokenService.getTokenExpiration(request));
    }

    /**
     * Validates a token belonging to a user of this Onyx node, as requested by
     * another node. The request itself should have an Authorization header
     * with a bearer token that proves the identity of the onyx node that's
     * requesting to verify the user.
     * @param request The HTTP request.
     * @param validationData The data needed to validate the user.
     * @return An object that tells whether the user is verified.
     */
    @PostMapping("/auth/validate-foreign-token")
    public Object validateToken(HttpServletRequest request, @RequestBody ForeignTokenValidationRequest validationData) {
        // TODO: Implement this!
        return null;
    }
}
