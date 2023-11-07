package com.andrewlalis.onyx.auth.model;

/**
 * Similar to a {@link User}, the NetworkUser contains the information for a
 * user who's coming to this node from another in the network. We don't have
 * as much information as with a normal user, but enough to work with.
 * @param id The user's id, as it is defined by its host Onyx node.
 * @param username The user's username, as it is defined by its host Onyx node.
 * @param displayName The user's display name.
 */
public record NetworkUser(
        long id,
        String username,
        String displayName
) {}
