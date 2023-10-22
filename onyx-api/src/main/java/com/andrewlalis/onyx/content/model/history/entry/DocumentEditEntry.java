package com.andrewlalis.onyx.content.model.history.entry;

import com.andrewlalis.onyx.content.model.ContentDocumentNode;
import com.andrewlalis.onyx.content.model.history.ContentNodeHistory;
import com.andrewlalis.onyx.content.model.history.ContentNodeHistoryEntry;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A history entry that records an edit that was made to a document.
 */
@Entity
@Table(name = "onyx_content_node_history_entry__document_edit")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DocumentEditEntry extends ContentNodeHistoryEntry {
    @Lob @Column(nullable = false, updatable = false)
    private byte[] diff;

    public DocumentEditEntry(ContentNodeHistory history, byte[] diff) {
        super(history);
        if (!(history.getContentNode() instanceof ContentDocumentNode)) {
            throw new IllegalArgumentException("Only the history of a ContentDocumentNode may be used to create a DocumentEditEntry.");
        }
        this.diff = diff;
    }
}
