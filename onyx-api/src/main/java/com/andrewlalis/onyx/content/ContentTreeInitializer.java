package com.andrewlalis.onyx.content;

import com.andrewlalis.onyx.content.dao.ContentNodeRepository;
import com.andrewlalis.onyx.content.model.access.ContentAccessLevel;
import com.andrewlalis.onyx.content.model.ContentContainerNode;
import com.andrewlalis.onyx.content.model.ContentNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ContentTreeInitializer implements CommandLineRunner {
    private final ContentNodeRepository contentNodeRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Check if content root container node exists.");
        if (!contentNodeRepository.existsByName(ContentNode.ROOT_NODE_NAME)) {
            ContentNode rootNode = new ContentContainerNode(ContentNode.ROOT_NODE_NAME, null);
            rootNode.getAccessInfo().setPublicAccessLevel(ContentAccessLevel.NONE);
            rootNode.getAccessInfo().setNetworkAccessLevel(ContentAccessLevel.NONE);
            rootNode.getAccessInfo().setNodeAccessLevel(ContentAccessLevel.NONE);
            contentNodeRepository.saveAndFlush(rootNode);
            log.info("Created content root container node.");
        }
    }
}
