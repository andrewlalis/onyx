package com.andrewlalis.onyx.content.dao;

import com.andrewlalis.onyx.content.model.access.ContentAccessRules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContentAccessRulesRepository extends JpaRepository<ContentAccessRules, Long> {
    Optional<ContentAccessRules> findByContentNodeId(long contentNodeId);
}
