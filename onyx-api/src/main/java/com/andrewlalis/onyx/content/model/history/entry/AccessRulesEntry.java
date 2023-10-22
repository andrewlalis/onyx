package com.andrewlalis.onyx.content.model.history.entry;

import com.andrewlalis.onyx.content.model.access.ContentAccessLevel;
import com.andrewlalis.onyx.content.model.history.ContentNodeHistory;
import com.andrewlalis.onyx.content.model.history.ContentNodeHistoryEntry;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A history entry that records a modification to a content node's access rules
 * by a user or the system.
 */
@Entity
@Table(name = "onyx_content_node_history_entry__access_rules")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccessRulesEntry extends ContentNodeHistoryEntry {
    @Enumerated(EnumType.ORDINAL) @Column(nullable = false, columnDefinition = "TINYINT NOT NULL")
    private ContentAccessLevel oldPublicAccessLevel;
    @Enumerated(EnumType.ORDINAL) @Column(nullable = false, columnDefinition = "TINYINT NOT NULL")
    private ContentAccessLevel oldNetworkAccessLevel;
    @Enumerated(EnumType.ORDINAL) @Column(nullable = false, columnDefinition = "TINYINT NOT NULL")
    private ContentAccessLevel oldNodeAccessLevel;

    @Enumerated(EnumType.ORDINAL) @Column(nullable = false, columnDefinition = "TINYINT NOT NULL")
    private ContentAccessLevel newPublicAccessLevel;
    @Enumerated(EnumType.ORDINAL) @Column(nullable = false, columnDefinition = "TINYINT NOT NULL")
    private ContentAccessLevel newNetworkAccessLevel;
    @Enumerated(EnumType.ORDINAL) @Column(nullable = false, columnDefinition = "TINYINT NOT NULL")
    private ContentAccessLevel newNodeAccessLevel;

    // TODO: add usersAllowed, usersDisallowed

    public AccessRulesEntry(
            ContentNodeHistory history,
            ContentAccessLevel oldPublicAccessLevel,
            ContentAccessLevel oldNetworkAccessLevel,
            ContentAccessLevel oldNodeAccessLevel,
            ContentAccessLevel newPublicAccessLevel,
            ContentAccessLevel newNetworkAccessLevel,
            ContentAccessLevel newNodeAccessLevel
    ) {
        super(history);
        this.oldPublicAccessLevel = oldPublicAccessLevel;
        this.oldNetworkAccessLevel = oldNetworkAccessLevel;
        this.oldNodeAccessLevel = oldNodeAccessLevel;
        this.newPublicAccessLevel = newPublicAccessLevel;
        this.newNetworkAccessLevel = newNetworkAccessLevel;
        this.newNodeAccessLevel = newNodeAccessLevel;
    }
}
