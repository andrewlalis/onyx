package com.andrewlalis.onyx.content.model.access;

import com.andrewlalis.onyx.auth.model.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "onyx_content_access_rules_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserContentAccessRule {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private ContentAccessRules contentAccessRules;

    @Enumerated(EnumType.ORDINAL) @Column(nullable = false, columnDefinition = "TINYINT NOT NULL") @Setter
    private ContentAccessLevel accessLevel;

    public UserContentAccessRule(User user, ContentAccessRules contentAccessRules, ContentAccessLevel accessLevel) {
        this.user = user;
        this.contentAccessRules = contentAccessRules;
        this.accessLevel = accessLevel;
    }
}
