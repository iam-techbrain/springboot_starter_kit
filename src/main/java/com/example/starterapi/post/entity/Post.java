package com.example.starterapi.post.entity;

import com.example.starterapi.common.entity.BaseAuditableEntity;
import com.example.starterapi.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

/**
 * Domain entity representing a blog/feed post.
 * Demonstrates OOP Inheritance from BaseAuditableEntity and JPA entity relationships.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "posts", indexes = {
        @Index(name = "idx_post_slug", columnList = "slug", unique = true),
        @Index(name = "idx_post_status", columnList = "status"),
        @Index(name = "idx_post_author", columnList = "author_id")
})
public class Post extends BaseAuditableEntity {

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, unique = true, length = 250)
    private String slug;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private PostStatus status = PostStatus.DRAFT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(name = "views_count", nullable = false)
    @Builder.Default
    private long viewsCount = 0L;

    public void incrementViews() {
        this.viewsCount++;
    }
}
