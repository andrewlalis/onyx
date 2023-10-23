package com.andrewlalis.onyx.content.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A type of content node that contains a single document.
 */
@Entity
@Table(name = "onyx_content_document_node")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ContentDocumentNode extends ContentNode {
    @Column(nullable = false, updatable = false, length = 127)
    private String contentType;

    /**
     * The raw file content for this document.
     */
    @Lob @Column(nullable = false) @Setter
    private byte[] content;

    public ContentDocumentNode(String contentType, byte[] content) {
        this.contentType = contentType;
        this.content = content;
    }
}
