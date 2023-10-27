package com.andrewlalis.onyx.auth.api;

public record AccessTokenResponse(
        String accessToken,
        long expiresAt
) {}
