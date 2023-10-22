package com.andrewlalis.onyx.content.model.history.entry;

import com.andrewlalis.onyx.content.model.history.ContentNodeHistory;
import com.andrewlalis.onyx.content.model.history.ContentNodeHistoryEntry;
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
@Table(name = "onyx_content_node_history_entry__archived")
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
