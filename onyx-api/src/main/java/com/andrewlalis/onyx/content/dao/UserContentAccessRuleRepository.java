package com.andrewlalis.onyx.content.dao;

import com.andrewlalis.onyx.content.model.access.UserContentAccessRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserContentAccessRuleRepository extends JpaRepository<UserContentAccessRule, Long> {
    @Query("SELECT ucar FROM UserContentAccessRule ucar " +
            "WHERE ucar.contentAccessRules.contentNode.id = :nodeId AND " +
            "ucar.user.id = :userId")
    Optional<UserContentAccessRule> findByContentNodeIdAndUser(long nodeId, long userId);

    @Query("SELECT ucar FROM UserContentAccessRule ucar WHERE ucar.contentAccessRules.contentNode.id IN :nodeIds")
    List<UserContentAccessRule> findAllByContentNodeIds(List<Long> nodeIds);
}
