package com.example.starterapi.post.dto;

import com.example.starterapi.post.entity.Post;
import com.example.starterapi.post.entity.PostStatus;
import com.example.starterapi.user.dto.UserProfileResponse;

import java.time.Instant;

/**
 * Public response representation of a Post entity.
 */
public record PostResponse(
        Long id,
        String title,
        String slug,
        String content,
        PostStatus status,
        long viewsCount,
        UserProfileResponse author,
        Instant createdAt,
        Instant updatedAt
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getSlug(),
                post.getContent(),
                post.getStatus(),
                post.getViewsCount(),
                UserProfileResponse.from(post.getAuthor()),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
