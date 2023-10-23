package com.andrewlalis.onyx.content.api;

import com.andrewlalis.onyx.content.service.ContentNodeService;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ContentNodeController {
    private final ContentNodeService contentNodeService;

    @GetMapping("/content/nodes/{nodeId}")
    public ContentNodeResponse getContentNode(@PathVariable long nodeId) {
        return contentNodeService.getNodeById(nodeId);
    }

    @GetMapping("/content/nodes/root")
    public ContentNodeResponse getRootNode() {
        return contentNodeService.getRootNode();
    }

    @PostMapping("/content/nodes/{nodeId}/children")
    public ContentNodeResponse createContentNode(@PathVariable long nodeId, @RequestBody ObjectNode body) {
        return contentNodeService.createNode(nodeId, body);
    }

    @DeleteMapping("/content/nodes/{nodeId}")
    public void deleteContentNode(@PathVariable long nodeId) {
        contentNodeService.deleteNode(nodeId);
    }
}
