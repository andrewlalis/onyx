package com.andrewlalis.onyx.auth.components;

import com.andrewlalis.onyx.auth.model.User;
import com.andrewlalis.onyx.content.dao.ContentNodeRepository;
import com.andrewlalis.onyx.content.model.ContentNode;
import com.andrewlalis.onyx.content.service.ContentAccessService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * A filter that's run on any request to "/content/nodes/:id/**", which checks
 * that the user accessing the specific node is actually allowed to access it,
 * based on the node's access rules.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ContentAccessFilter extends OncePerRequestFilter {
    private final ContentNodeRepository contentNodeRepository;
    private final ContentAccessService contentAccessService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        log.info("Calling ContentAccessFilter for path {}", request.getServletPath());
        String nodeId = extractNodeId(request);
        if (nodeId == null) {
            log.warn("Couldn't extract node id!");
            return;
        }
        log.info("Extracted node id: {}", nodeId);
        ContentNode node;
        if (nodeId.equalsIgnoreCase("root")) {
            node = contentNodeRepository.findRoot();
        } else {
            node = contentNodeRepository.findById(Long.parseLong(nodeId)).orElse(null);
        }
        if (node == null) {
            log.warn("Node doesn't exist!");
            return;
        }
        // TODO: Actually check access rules.
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().startsWith("/content/nodes/");
    }

    private String extractNodeId(HttpServletRequest request) {
        String path = request.getServletPath();
        final String PREFIX = "/content/nodes/";
        if (!path.startsWith(PREFIX)) return null;
        String suffix = path.substring(PREFIX.length());
        int suffixEnd = suffix.indexOf('/');
        if (suffixEnd != -1) return suffix.substring(0, suffixEnd);
        return suffix;
    }
}
