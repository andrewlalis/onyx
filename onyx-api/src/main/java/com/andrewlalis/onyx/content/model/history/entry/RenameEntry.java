package com.andrewlalis.onyx.content.model.history.entry;

import com.andrewlalis.onyx.content.model.ContentNode;
import com.andrewlalis.onyx.content.model.history.ContentNodeHistory;
import com.andrewlalis.onyx.content.model.history.ContentNodeHistoryEntry;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "onyx_content_node_history_entry__rename")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RenameEntry extends ContentNodeHistoryEntry {
    @Column(nullable = false, updatable = false, length = ContentNode.MAX_NAME_LENGTH)
    private String oldName;

    @Column(nullable = false, updatable = false, length = ContentNode.MAX_NAME_LENGTH)
    private String newName;

    public RenameEntry(ContentNodeHistory history, String oldName, String newName) {
        super(history);
        this.oldName = oldName;
        this.newName = newName;
    }
}
