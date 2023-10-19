package com.andrewlalis.onyx.content.history;

import com.andrewlalis.onyx.content.ContentNode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * An entity that represents the complete history of a single content node,
 * storing an ordered list of entries detailing how that node has changed.
 */
@Entity
@Table(name = "content_node_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContentNodeHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(mappedBy = "history")
    private ContentNode contentNode;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "history")
    private Set<ContentNodeHistoryEntry> entries;

    public ContentNodeHistory(ContentNode contentNode) {
        this.contentNode = contentNode;
        this.entries = new HashSet<>();
    }
}
