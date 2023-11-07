package com.andrewlalis.onyx.auth.components;

import com.andrewlalis.onyx.auth.model.User;

/**
 * A type of token authentication that's used for users of this Onyx node,
 * where we have full access to the user and their info.
 */
public class LocalUserAuth extends TokenAuth {
    private final User user;

    public LocalUserAuth(String token, User user) {
        super(token);
        this.user = user;
    }

    @Override
    public User getPrincipal() {
        return user;
    }

    @Override
    public String getName() {
        return user.getUsername();
    }
}
