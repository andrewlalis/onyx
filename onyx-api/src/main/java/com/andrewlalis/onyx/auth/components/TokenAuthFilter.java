package com.andrewlalis.onyx.auth.components;

import com.andrewlalis.onyx.auth.model.User;
import com.andrewlalis.onyx.auth.service.TokenService;
import com.andrewlalis.onyx.auth.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/**
 * A filter that extracts and verifies an HTTP request's JWT, and uses that
 * to set the current security context's authentication instance accordingly.
 * <p>
 *     If the JWT originates from this Onyx node, then we simply fetch the User
 *     and set a new {@link LocalUserAuth}.
 * </p>
 * <p>
 *     If the JWT originates from a networked Onyx node, then we'll try and
 *     confirm with the original node that the JWT is valid, and then set a new
 *     {@link NetworkUserAuth}.
 * </p>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TokenAuthFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        final var securityContext = SecurityContextHolder.getContext();
        boolean authContextSet = false;
        try {
            String token = tokenService.extractBearerToken(request);
            Jws<Claims> jws = tokenService.parseToken(token);
            if (jws != null) {
                long userId = Long.parseLong(jws.getBody().getSubject());
                Optional<User> optionalUser = userService.findById(userId);
                if (optionalUser.isPresent()) {
                    securityContext.setAuthentication(new LocalUserAuth(token, optionalUser.get()));
                    authContextSet = true;
                }
            }
        } catch (Exception e) {
            log.warn("Exception occurred in JwtFilter.", e);
        }
        // TODO: Check if the request is coming from a network user, then validate their token.
        if (!authContextSet) {
            securityContext.setAuthentication(new PublicUserAuth());
        }
        filterChain.doFilter(request, response);
    }
}
