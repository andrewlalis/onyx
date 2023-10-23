package com.andrewlalis.onyx;

import com.andrewlalis.onyx.auth.model.User;
import com.andrewlalis.onyx.auth.model.UserRepository;
import com.andrewlalis.onyx.content.dao.ContentNodeRepository;
import com.andrewlalis.onyx.content.model.ContentContainerNode;
import com.andrewlalis.onyx.content.model.ContentNode;
import com.andrewlalis.onyx.content.model.access.ContentAccessLevel;
import com.andrewlalis.onyx.content.model.access.UserContentAccessRule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * A component that runs on application startup, to take care of some
 * first-time initialization, if needed:
 * <ol>
 *     <li>Create a default admin user, if none exists.</li>
 *     <li>Create the root content node for the content tree.</li>
 * </ol>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class NodeInitializer implements CommandLineRunner {
    private final ContentNodeRepository contentNodeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        User adminUser = createAdminUserIfNoUsers();
        createRootNodeIfNotExists(adminUser);
    }

    private void createRootNodeIfNotExists(User adminUser) {
        if (!contentNodeRepository.existsByName(ContentNode.ROOT_NODE_NAME)) {
            ContentNode rootNode = new ContentContainerNode(ContentNode.ROOT_NODE_NAME, null);
            rootNode.getAccessInfo().setPublicAccessLevel(ContentAccessLevel.NONE);
            rootNode.getAccessInfo().setNetworkAccessLevel(ContentAccessLevel.NONE);
            rootNode.getAccessInfo().setNodeAccessLevel(ContentAccessLevel.NONE);
            if (adminUser != null) {
                rootNode.getAccessInfo().getUserAccessRules().add(new UserContentAccessRule(
                        adminUser,
                        rootNode.getAccessInfo(),
                        ContentAccessLevel.EDIT
                ));
            }
            contentNodeRepository.saveAndFlush(rootNode);
            log.info("Created content root container node.");
        }
    }

    private User createAdminUserIfNoUsers() {
        if (userRepository.count() == 0) {
            User user = userRepository.saveAndFlush(new User(
                    "admin",
                    "Admin",
                    passwordEncoder.encode("onyx-admin")
            ));
            log.info("Created user \"admin\" with password \"onyx-admin\"");
            return user;
        }
        return null;
    }
}
