package com.andrewlalis.onyx.content.history.entry;

import com.andrewlalis.onyx.content.ContentAccessLevel;
import com.andrewlalis.onyx.content.history.ContentNodeHistory;
import com.andrewlalis.onyx.content.history.ContentNodeHistoryEntry;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A history entry that records a modification to a content node's access rules
 * by a user or the system.
 */
@Entity
@Table(name = "content_node_history_entry_access_rules")
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
