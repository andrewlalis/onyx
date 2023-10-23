package com.andrewlalis.onyx.content.service;

import com.andrewlalis.onyx.content.api.ContentNodeResponse;
import com.andrewlalis.onyx.content.dao.ContentNodeRepository;
import com.andrewlalis.onyx.content.model.ContentContainerNode;
import com.andrewlalis.onyx.content.model.ContentDocumentNode;
import com.andrewlalis.onyx.content.model.ContentNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class ContentNodeService {
    private final ContentNodeRepository contentNodeRepository;

    @Transactional(readOnly = true)
    public ContentNodeResponse getNodeById(long nodeId) {
        ContentNode node = contentNodeRepository.findById(nodeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ContentNodeResponse.forNode(node);
    }

    @Transactional(readOnly = true)
    public ContentNodeResponse getRootNode() {
        ContentNode rootNode = contentNodeRepository.findRoot();
        return ContentNodeResponse.forNode(rootNode);
    }

    @Transactional
    public ContentNodeResponse createNode(long parentNodeId, ObjectNode body) {
        ContentNode parentNode = contentNodeRepository.findById(parentNodeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (!(parentNode instanceof ContentContainerNode container)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can only create new content nodes inside containers.");
        }
        try {
            ContentNode node;
            // First validate that the name is legitimate, and doesn't conflict with any siblings.
            String name = body.get("name").asText().trim();
            if (name.isBlank() || name.length() > ContentNode.MAX_NAME_LENGTH || name.equalsIgnoreCase(ContentNode.ROOT_NODE_NAME)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid name.");
            }
            for (ContentNode child : container.getChildren()) {
                if (child.getName().equals(name)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate name.");
                }
            }

            String nodeTypeStr = body.get("nodeType").asText();
            if (nodeTypeStr.equalsIgnoreCase(ContentNode.Type.CONTAINER.name())) {
                node = new ContentContainerNode(name, container);
            } else if (nodeTypeStr.equalsIgnoreCase(ContentNode.Type.DOCUMENT.name())) {
                String contentType = body.get("contentType").asText();
                String content = body.get("content").asText();
                node = new ContentDocumentNode(contentType, content.getBytes(StandardCharsets.UTF_8));
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid nodeType.");
            }
            node = contentNodeRepository.save(node);
            return ContentNodeResponse.forNode(node);
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request body.", e);
        }
    }

    @Transactional
    public void deleteNode(long nodeId) {
        ContentNode node = contentNodeRepository.findById(nodeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (node.getName().equals(ContentNode.ROOT_NODE_NAME)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot delete the root node.");
        }
        contentNodeRepository.delete(node);
    }
}
