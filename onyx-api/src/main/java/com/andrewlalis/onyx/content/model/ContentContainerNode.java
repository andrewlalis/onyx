package com.andrewlalis.onyx.content.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * A type of content node that contains a list of children, which could
 * themselves be any type of content node.
 */
@Entity
@Table(name = "onyx_content_container_node")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public final class ContentContainerNode extends ContentNode {
    /**
     * The set of children that belong to this container.
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "parentContainer")
    private Set<ContentNode> children;

    public ContentContainerNode(String name, ContentContainerNode parentContainer) {
        super(name, Type.CONTAINER, parentContainer);
        this.children = new HashSet<>();
    }
}
