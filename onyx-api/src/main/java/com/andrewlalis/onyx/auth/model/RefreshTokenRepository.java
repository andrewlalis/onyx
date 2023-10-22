package com.andrewlalis.onyx.auth.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Iterable<RefreshToken> findAllByTokenSuffix(String suffix);
    void deleteAllByUserId(long userId);
}
