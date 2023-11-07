package com.andrewlalis.onyx.auth.components;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

/**
 * An abstract base class for any authentication instance based on the use of
 * a JWT for authentication.
 */
public abstract class TokenAuth implements Authentication {
    public final String token;

    protected TokenAuth(String token) {
        this.token = token;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new RuntimeException("Cannot set the authenticated status of TokenAuthentication.");
    }
}
