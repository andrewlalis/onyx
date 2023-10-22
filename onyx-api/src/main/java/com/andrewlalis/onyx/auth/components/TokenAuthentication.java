package com.andrewlalis.onyx.auth.components;

import com.andrewlalis.onyx.auth.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

/**
 * The authentication implementation that's used when a user logs in with an
 * access token.
 * @param user The user that the token belongs to.
 * @param jws The raw token.
 */
public record TokenAuthentication(User user, Jws<Claims> jws) implements Authentication {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public Object getCredentials() {
        return this.jws;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public User getPrincipal() {
        return this.user;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new RuntimeException("Cannot set the authenticated status of TokenAuthentication.");
    }

    @Override
    public String getName() {
        return user.getUsername();
    }
}
