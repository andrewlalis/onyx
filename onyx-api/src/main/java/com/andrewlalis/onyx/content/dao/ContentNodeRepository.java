package com.andrewlalis.onyx.content.dao;

import com.andrewlalis.onyx.content.model.ContentNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentNodeRepository extends JpaRepository<ContentNode, Long> {
    boolean existsByName(String name);
}
