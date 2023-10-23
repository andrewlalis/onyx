package com.andrewlalis.onyx.content.api;

import com.andrewlalis.onyx.content.model.ContentContainerNode;
import com.andrewlalis.onyx.content.model.ContentDocumentNode;
import com.andrewlalis.onyx.content.model.ContentNode;

import java.util.Set;

public abstract class ContentNodeResponse {
    public final long id;
    public final String name;
    public final String nodeType;
    public final boolean archived;

    public ContentNodeResponse(ContentNode node) {
        this.id = node.getId();
        this.name = node.getName();
        this.nodeType = node.getNodeType().name();
        this.archived = node.isArchived();
    }

    public static ContentNodeResponse forNode(ContentNode node) {
        return switch (node) {
            case ContentContainerNode c -> new ContentNodeResponse.Container(c);
            case ContentDocumentNode d -> new ContentNodeResponse.Document(d);
            default -> throw new IllegalStateException("Unexpected value: " + node);
        };
    }

    public static class Container extends ContentNodeResponse {
        public record ChildInfo (long id, String name, String nodeType) {}

        public final ChildInfo[] children;

        public Container(ContentContainerNode node) {
            super(node);
            Set<ContentNode> childrenSet = node.getChildren();
            this.children = new ChildInfo[childrenSet.size()];
            int i = 0;
            for (ContentNode child : childrenSet) {
                children[i++] = new ChildInfo(
                        child.getId(),
                        child.getName(),
                        child.getNodeType().name()
                );
            }
        }
    }

    public static class Document extends ContentNodeResponse {
        public final String contentType;

        public Document(ContentDocumentNode node) {
            super(node);
            this.contentType = node.getContentType();
        }
    }
}
