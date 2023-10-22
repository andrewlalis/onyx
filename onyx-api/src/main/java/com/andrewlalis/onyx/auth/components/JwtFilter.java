package com.andrewlalis.onyx.auth.components;

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

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            Jws<Claims> jws = tokenService.getToken(request);
            if (jws != null) {
                long userId = Long.parseLong(jws.getBody().getSubject());
                userService.findById(userId).ifPresent(user -> SecurityContextHolder.getContext()
                        .setAuthentication(new TokenAuthentication(user, jws))
                );
            }
        } catch (Exception e) {
            log.warn("Exception occurred in JwtFilter.", e);
        }
        filterChain.doFilter(request, response);
    }
}
