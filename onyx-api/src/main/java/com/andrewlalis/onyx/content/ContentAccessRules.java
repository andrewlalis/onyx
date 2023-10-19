package com.andrewlalis.onyx.content;

import jakarta.persistence.*;
import lombok.Getter;

/**
 * An entity meant to be attached to a {@link ContentNode}, which contains the
 * access levels for the different ways in which a content node can be accessed.
 */
@Entity
@Table(name = "content_access_rules")
@Getter
public class ContentAccessRules {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * The content node that these access rules apply to.
     */
    @OneToOne(mappedBy = "accessInfo", fetch = FetchType.LAZY)
    private ContentNode contentNode;

    /** The access level that applies to users from outside this onyx network. */
    @Enumerated(EnumType.ORDINAL) @Column(nullable = false, columnDefinition = "TINYINT NOT NULL")
    private ContentAccessLevel publicAccessLevel = ContentAccessLevel.INHERIT;

    /** The access level that applies to users from within this onyx network. */
    @Enumerated(EnumType.ORDINAL) @Column(nullable = false, columnDefinition = "TINYINT NOT NULL")
    private ContentAccessLevel networkAccessLevel = ContentAccessLevel.INHERIT;

    /** The access level that applies to users within only this onyx node. */
    @Enumerated(EnumType.ORDINAL) @Column(nullable = false, columnDefinition = "TINYINT NOT NULL")
    private ContentAccessLevel nodeAccessLevel = ContentAccessLevel.INHERIT;

    // TODO: Add a user allowlist.

}
