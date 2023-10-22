package com.andrewlalis.onyx.content.model.access;

/**
 * The different levels of access that can be set for content in an onyx node.
 */
public enum ContentAccessLevel {
    /**
     * An access level that does not permit reading or editing the content.
     */
    NONE,

    /**
     * An access level that permits reading content, but not editing.
     */
    VIEW,

    /**
     * An access level that permits reading and editing content.
     */
    EDIT,

    /**
     * An access level that takes the parent node's access level.
     */
    INHERIT
}
