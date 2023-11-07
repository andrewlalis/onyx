package com.andrewlalis.onyx.content.service;

import com.andrewlalis.onyx.auth.model.User;
import com.andrewlalis.onyx.content.dao.ContentAccessRulesRepository;
import com.andrewlalis.onyx.content.dao.ContentNodeRepository;
import com.andrewlalis.onyx.content.dao.UserContentAccessRuleRepository;
import com.andrewlalis.onyx.content.model.ContentNode;
import com.andrewlalis.onyx.content.model.access.ContentAccessLevel;
import com.andrewlalis.onyx.content.model.access.ContentAccessRules;
import com.andrewlalis.onyx.content.model.access.UserContentAccessRule;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A service that's responsible for determining if a user has permission to
 * interact with certain content nodes.
 * TODO: Add some sort of caching so recursive traversal of the content graph isn't needed.
 */
@Service
@RequiredArgsConstructor
public class ContentAccessService {
    private final ContentAccessRulesRepository accessRulesRepository;
    private final ContentNodeRepository contentNodeRepository;
    private final UserContentAccessRuleRepository userContentAccessRuleRepository;

    @Transactional(readOnly = true)
    public boolean currentAuthCanReadContent(long nodeId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var a = getEffectiveAccessLevel(user, nodeId);
        return a == ContentAccessLevel.VIEW || a == ContentAccessLevel.EDIT;
    }

    @Transactional(readOnly = true)
    public boolean currentAuthCanEditContent(long nodeId) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var a = getEffectiveAccessLevel(user, nodeId);
        return a == ContentAccessLevel.EDIT;
    }

    /**
     * Gets the effective access level that a user has to a particular content
     * node. It finds this using the following algorithm:
     * <ol>
     *     <li>Get a list of ids for this node and all its parents.</li>
     *     <li>
     *         First, recursively check the user-specific access levels for
     *         this and all parent nodes. If there exists a node with a user-
     *         specific access level for the user, then that's returned.
     *     </li>
     *     <li>
     *         Otherwise, recursively check the generic access levels for this
     *         and all parent nodes. The first non-INHERIT access level is
     *         returned.
     *     </li>
     * </ol>
     * The root node should not logically ever have an INHERIT access level, so
     * it's the last resort if no others are found.
     * @param user The user to get the access level for.
     * @param nodeId The id of the content node to get the access level for.
     * @return The access level that the given user has to the given node.
     */
    private ContentAccessLevel getEffectiveAccessLevel(User user, long nodeId) {
        List<Long> nodeIds = getAllNodeIds(nodeId);
        for (long nId : nodeIds) {
            Optional<UserContentAccessRule> userAccessRule = userContentAccessRuleRepository.findByContentNodeIdAndUser(nId, user.getId());
            if (userAccessRule.isPresent() && userAccessRule.get().getAccessLevel() != ContentAccessLevel.INHERIT) {
                return userAccessRule.get().getAccessLevel();
            }
        }
        for (long nId : nodeIds) {
            ContentAccessRules accessRules = accessRulesRepository.findByContentNodeId(nId).orElseThrow();
            // TODO: Check the user's origin: anonymous, network, or node.
            // For now, we assume node.
            if (accessRules.getNodeAccessLevel() != ContentAccessLevel.INHERIT) {
                return accessRules.getNodeAccessLevel();
            }
        }
        return ContentAccessLevel.NONE;
    }

    private List<Long> getAllNodeIds(long nodeId) {
        List<Long> nodeIds = new ArrayList<>();
        nodeIds.add(nodeId);
        ContentNode node = contentNodeRepository.findById(nodeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ContentNode parent = node.getParentContainer();
        while (parent != null) {
            nodeIds.add(parent.getId());
            parent = parent.getParentContainer();
        }
        return nodeIds;
    }

    private enum ContentAccessType {
        PUBLIC,
        NETWORK,
        NODE
    }
}
