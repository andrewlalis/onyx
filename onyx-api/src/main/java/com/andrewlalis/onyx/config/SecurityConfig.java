package com.andrewlalis.onyx.config;

import com.andrewlalis.onyx.auth.components.ContentAccessFilter;
import com.andrewlalis.onyx.auth.components.TokenAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenAuthFilter tokenAuthFilter;
    private final ContentAccessFilter contentAccessFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(registry -> {
            // Public endpoints that require no authentication.
            registry.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/auth/login")).permitAll();
            registry.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/auth/access")).permitAll();
            registry.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/auth/token-expiration")).permitAll();

            // Any path not explicitly listed here requires authentication to access.
            registry.anyRequest().authenticated();
        });
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.NEVER));
        http.addFilterBefore(tokenAuthFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(contentAccessFilter, TokenAuthFilter.class);
        http.cors(configurer -> configurer.configure(http));
        return http.build();
    }
}
