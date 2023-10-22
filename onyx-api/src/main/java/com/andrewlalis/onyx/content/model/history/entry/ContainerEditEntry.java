package com.andrewlalis.onyx.content.model.history.entry;

import com.andrewlalis.onyx.content.model.ContentContainerNode;
import com.andrewlalis.onyx.content.model.ContentNode;
import com.andrewlalis.onyx.content.model.history.ContentNodeHistory;
import com.andrewlalis.onyx.content.model.history.ContentNodeHistoryEntry;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * History entry for tracking updates to the contents of a container node.
 */
@Entity
@Table(name = "onyx_content_node_history_entry__container_edit")
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
        if (!(history.getContentNode() instanceof ContentContainerNode)) {
            throw new IllegalArgumentException("Only the history of a ContentContainerNode may be used to create a ContainerEditEntry.");
        }
        this.type = type;
        this.affectedNodeName = affectedNodeName;
    }
}
