package com.andrewlalis.onyx.config;

import com.andrewlalis.onyx.auth.components.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(registry -> {
            registry.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/auth/login")).permitAll();
            registry.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/auth/access")).permitAll();
            registry.anyRequest().authenticated();
        });
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.NEVER));
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.cors(configurer -> configurer.configure(http));
        return http.build();
    }
}
