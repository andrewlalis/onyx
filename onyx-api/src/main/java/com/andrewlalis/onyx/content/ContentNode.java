package com.andrewlalis.onyx.content;

import com.andrewlalis.onyx.content.history.ContentNodeHistory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The abstract model that represents all nodes in the system's hierarchical
 * content tree, including both <em>containers</em> and <em>documents</em>.
 */
@Entity
@Table(name = "content_node")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ContentNode {
    public static final int MAX_NAME_LENGTH = 127;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = MAX_NAME_LENGTH)
    private String name;

    @Enumerated(EnumType.ORDINAL) @Column(columnDefinition = "TINYINT NOT NULL")
    private Type type;

    @OneToOne(fetch = FetchType.LAZY, optional = false, orphanRemoval = true, cascade = CascadeType.ALL)
    private ContentAccessRules accessInfo;

    /**
     * The container node that this one belongs to. This will be null <strong>only</strong>
     * in the case of the root node, which is a special, hidden container node
     * that acts as the top-level of the content hierarchy.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private ContentContainerNode parentContainer;

    @OneToOne(fetch = FetchType.LAZY, optional = false, orphanRemoval = true, cascade = CascadeType.ALL)
    private ContentNodeHistory history;

    @Column(nullable = false)
    private boolean archived;

    public enum Type {
        CONTAINER,
        DOCUMENT
    }

    public ContentNode(String name, Type type, ContentContainerNode parentContainer) {
        this.name = name;
        this.type = type;
        this.parentContainer = parentContainer;
        this.history = new ContentNodeHistory(this);
    }
}
