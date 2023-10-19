package com.andrewlalis.onyx.content.history.entry;

import com.andrewlalis.onyx.content.history.ContentNodeHistory;
import com.andrewlalis.onyx.content.history.ContentNodeHistoryEntry;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * History entry for tracking the state of a content node's `archived` status.
 */
@Entity
@Table(name = "content_node_history_entry_archived")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArchivedEntry extends ContentNodeHistoryEntry {
    @Column(nullable = false, updatable = false)
    private boolean archived;

    public ArchivedEntry(ContentNodeHistory history, boolean archived) {
        super(history);
        this.archived = archived;
    }
}
