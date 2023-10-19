package com.andrewlalis.onyx.content;

import jakarta.persistence.*;

import java.util.Set;

/**
 * A type of content node that contains a list of children, which could
 * themselves be any type of content node.
 */
@Entity
@Table(name = "content_node_container")
public final class ContentContainerNode extends ContentNode {
    /**
     * The set of children that belong to this container.
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "parentContainer")
    private Set<ContentNode> children;
}
