package com.andrewlalis.onyx.auth.api;

public record TokenPair(
        String refreshToken,
        long refreshTokenExpiresAt,
        String accessToken,
        long accessTokenExpiresAt
) {}
