package com.andrewlalis.onyx.auth.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "onyx_auth_refresh_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RefreshToken {
    public static final int SUFFIX_LENGTH = 7;

    @Id
    private String hash;

    @Column(nullable = false, updatable = false, length = SUFFIX_LENGTH)
    private String tokenSuffix;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime expiresAt;

    public RefreshToken(String hash, String tokenSuffix, User user, LocalDateTime expiresAt) {
        this.hash = hash;
        this.tokenSuffix = tokenSuffix;
        this.user = user;
        this.expiresAt = expiresAt;
    }
}
