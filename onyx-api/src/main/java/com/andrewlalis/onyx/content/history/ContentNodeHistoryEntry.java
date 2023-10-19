package com.andrewlalis.onyx.content.history;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * The base class for all types of content node history entries. It defines
 * some basic properties that all entries should have.
 */
@Entity
@Table(name = "content_node_history_entry")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ContentNodeHistoryEntry {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @CreationTimestamp
    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private ContentNodeHistory history;

    // TODO: Add user info (or system)

    public ContentNodeHistoryEntry(ContentNodeHistory history) {
        this.history = history;
    }
}
