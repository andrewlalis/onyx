package com.andrewlalis.onyx.content;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

/**
 * A type of content node that contains a single document.
 */
@Entity
@Table(name = "content_node_document")
public class ContentDocumentNode extends ContentNode {
    @Column(nullable = false, updatable = false, length = 127)
    private String type;

    /**
     * The raw file content for this document.
     */
    @Lob @Column(nullable = false)
    private byte[] content;
}
