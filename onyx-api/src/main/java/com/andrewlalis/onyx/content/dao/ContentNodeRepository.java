package com.andrewlalis.onyx.content.dao;

import com.andrewlalis.onyx.content.model.ContentNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContentNodeRepository extends JpaRepository<ContentNode, Long> {
    boolean existsByName(String name);

    @Query("SELECT cn FROM ContentNode cn WHERE cn.name = '" + ContentNode.ROOT_NODE_NAME + "'")
    ContentNode findRoot();

    interface ParentContainerId {
        long getParentContainerId();
    }

    @Query("SELECT cn.parentContainer.id FROM ContentNode cn WHERE cn.id = :nodeId")
    ParentContainerId getParentId(long nodeId);
}
