package com.andrewlalis.onyx.auth.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * The main authentication principal entity, representing a single user that
 * has an account on this onyx node.
 */
@Entity
@Table(name = "onyx_auth_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {
    public static final int MAX_USERNAME_LENGTH = 32;
    public static final int MAX_DISPLAY_NAME_LENGTH = 64;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true, length = MAX_USERNAME_LENGTH)
    private String username;

    @Column(nullable = false, length = MAX_DISPLAY_NAME_LENGTH)
    private String displayName;

    @Column(nullable = false, length = 60)
    private String passwordHash;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public User(String username, String displayName, String passwordHash) {
        this.username = username;
        this.displayName = displayName;
        this.passwordHash = passwordHash;
    }
}
