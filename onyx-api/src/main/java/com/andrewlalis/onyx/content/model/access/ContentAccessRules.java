package com.andrewlalis.onyx.content.model.access;

import com.andrewlalis.onyx.content.model.ContentNode;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * An entity meant to be attached to a {@link ContentNode}, which contains the
 * access levels for the different ways in which a content node can be accessed.
 */
@Entity
@Table(name = "onyx_content_access_rules")
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
    @Enumerated(EnumType.ORDINAL) @Column(nullable = false, columnDefinition = "TINYINT NOT NULL") @Setter
    private ContentAccessLevel publicAccessLevel = ContentAccessLevel.INHERIT;

    /** The access level that applies to users from within this onyx network. */
    @Enumerated(EnumType.ORDINAL) @Column(nullable = false, columnDefinition = "TINYINT NOT NULL") @Setter
    private ContentAccessLevel networkAccessLevel = ContentAccessLevel.INHERIT;

    /** The access level that applies to users within only this onyx node. */
    @Enumerated(EnumType.ORDINAL) @Column(nullable = false, columnDefinition = "TINYINT NOT NULL") @Setter
    private ContentAccessLevel nodeAccessLevel = ContentAccessLevel.INHERIT;

    /**
     * User-specific access rules that override other more generic rules, if present.
     */
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "contentAccessRules")
    private Set<UserContentAccessRule> userAccessRules = new HashSet<>();
}
