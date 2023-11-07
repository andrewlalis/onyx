package com.andrewlalis.onyx.auth.components;

import com.andrewlalis.onyx.auth.model.NetworkUser;

/**
 * A type of token authentication that's used for users of Onyx nodes networked
 * with this one. We don't have full access to their user data, but we can talk
 * to the networked node to get some basic information about the user.
 */
public class NetworkUserAuth extends TokenAuth {
    private final NetworkUser user;

    public NetworkUserAuth(String token, NetworkUser user) {
        super(token);
        this.user = user;
    }

    @Override
    public NetworkUser getPrincipal() {
        return user;
    }

    @Override
    public String getName() {
        return user.username();
    }
}
