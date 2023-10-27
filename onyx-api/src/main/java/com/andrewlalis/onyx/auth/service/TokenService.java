package com.andrewlalis.onyx.auth.service;

import com.andrewlalis.onyx.auth.api.AccessTokenResponse;
import com.andrewlalis.onyx.auth.api.LoginRequest;
import com.andrewlalis.onyx.auth.api.TokenPair;
import com.andrewlalis.onyx.auth.model.RefreshToken;
import com.andrewlalis.onyx.auth.model.RefreshTokenRepository;
import com.andrewlalis.onyx.auth.model.User;
import com.andrewlalis.onyx.auth.model.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String ISSUER = "Onyx API";
    private static final int REFRESH_TOKEN_EXPIRATION_DAYS = 30;
    private static final int ACCESS_TOKEN_EXPIRATION_MINUTES = 120;

    private PrivateKey signingKey;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Transactional
    public TokenPair generateTokenPair(LoginRequest request) {
        Optional<User> optionalUser = userRepository.findByUsername(request.username());
        if (
            optionalUser.isEmpty() ||
            !passwordEncoder.matches(request.password(), optionalUser.get().getPasswordHash())
        ) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials.");
        }
        User user = optionalUser.get();
        final Instant now = OffsetDateTime.now(ZoneOffset.UTC).toInstant();
        Instant refreshTokenExpiration = now.plus(REFRESH_TOKEN_EXPIRATION_DAYS, ChronoUnit.DAYS);
        String refreshToken = generateRefreshToken(user, refreshTokenExpiration);
        Instant accessTokenExpiration = now.plus(ACCESS_TOKEN_EXPIRATION_MINUTES, ChronoUnit.MINUTES);
        String accessToken = generateAccessToken(refreshToken, accessTokenExpiration);
        return new TokenPair(refreshToken, refreshTokenExpiration.toEpochMilli(), accessToken, accessTokenExpiration.toEpochMilli());
    }

    @Transactional
    public String generateRefreshToken(User user, Instant expiresAt) {
        try {
            String token = Jwts.builder()
                    .setSubject(Long.toString(user.getId()))
                    .setIssuer(ISSUER)
                    .setAudience("Onyx App")
                    .setExpiration(Date.from(expiresAt))
                    .claim("username", user.getUsername())
                    .claim("token-type", "refresh")
                    .signWith(getSigningKey())
                    .compact();
            refreshTokenRepository.saveAndFlush(new RefreshToken(
                    passwordEncoder.encode(token),
                    token.substring(token.length() - RefreshToken.SUFFIX_LENGTH),
                    user,
                    LocalDateTime.ofInstant(expiresAt, ZoneOffset.UTC)
            ));
            return token;
        } catch (Exception e) {
            log.error("Failed to generate refresh token.", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate token.");
        }
    }

    @Transactional(readOnly = true)
    public String generateAccessToken(String refreshTokenString, Instant expiresAt) {
        String suffix = refreshTokenString.substring(refreshTokenString.length() - RefreshToken.SUFFIX_LENGTH);
        RefreshToken refreshToken = null;
        for (RefreshToken possibleToken : refreshTokenRepository.findAllByTokenSuffix(suffix)) {
            if (passwordEncoder.matches(refreshTokenString, possibleToken.getHash())) {
                refreshToken = possibleToken;
                break;
            }
        }
        if (refreshToken == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unknown refresh token.");
        }
        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now(ZoneOffset.UTC))) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token is expired.");
        }
        try {
            return Jwts.builder()
                    .setSubject(Long.toString(refreshToken.getUser().getId()))
                    .setIssuer(ISSUER)
                    .setAudience("Onyx App")
                    .setExpiration(Date.from(expiresAt))
                    .claim("username", refreshToken.getUser().getUsername())
                    .claim("token-type", "access")
                    .signWith(getSigningKey())
                    .compact();
        } catch (Exception e) {
            log.error("Failed to generate access token.", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate token.");
        }
    }

    @Transactional(readOnly = true)
    public AccessTokenResponse generateAccessToken(HttpServletRequest request) {
        String refreshTokenString = extractBearerToken(request);
        if (refreshTokenString == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing refresh token.");
        }
        Instant expiresAt = OffsetDateTime.now(ZoneOffset.UTC).plusMinutes(ACCESS_TOKEN_EXPIRATION_MINUTES).toInstant();
        return new AccessTokenResponse(
                generateAccessToken(refreshTokenString, expiresAt),
                expiresAt.toEpochMilli()
        );
    }

    @Transactional
    public void removeAllRefreshTokens(User user) {
        refreshTokenRepository.deleteAllByUserId(user.getId());
    }

    public long getTokenExpiration(HttpServletRequest request) {
        try {
            Jws<Claims> jws = getToken(request);
            return jws.getBody().getExpiration().getTime();
        } catch (Exception e) {
            log.warn("Exception occurred while getting token expiration.", e);
            return -1;
        }
    }

    public Jws<Claims> getToken(HttpServletRequest request) throws Exception {
        String rawToken = extractBearerToken(request);
        if (rawToken == null) return null;
        JwtParserBuilder parserBuilder = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .requireIssuer(ISSUER);
        return parserBuilder.build().parseClaimsJws(rawToken);
    }

    private String extractBearerToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) return null;
        String rawToken = authorizationHeader.substring(BEARER_PREFIX.length());
        if (rawToken.isBlank()) return null;
        return rawToken;
    }

    private PrivateKey getSigningKey() throws Exception {
        if (signingKey == null) {
            byte[] keyBytes = Files.readAllBytes(Path.of("private_key.der"));
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            signingKey = kf.generatePrivate(spec);
        }
        return signingKey;
    }
}
