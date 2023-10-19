package com.andrewlalis.onyx.content.history.entry;

import com.andrewlalis.onyx.content.ContentNode;
import com.andrewlalis.onyx.content.history.ContentNodeHistory;
import com.andrewlalis.onyx.content.history.ContentNodeHistoryEntry;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * History entry for tracking updates to the contents of a container node.
 */
@Entity
@Table(name = "content_node_history_entry_container_edit")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContainerEditEntry extends ContentNodeHistoryEntry {
    public enum EditType {
        CONTAINER_ADDED,
        CONTAINER_REMOVED,
        DOCUMENT_ADDED,
        DOCUMENT_REMOVED
    }

    @Enumerated(EnumType.ORDINAL)
    private EditType type;

    @Column(nullable = false, updatable = false, length = ContentNode.MAX_NAME_LENGTH)
    private String affectedNodeName;

    public ContainerEditEntry(ContentNodeHistory history, EditType type, String affectedNodeName) {
        super(history);
        this.type = type;
        this.affectedNodeName = affectedNodeName;
    }
}
