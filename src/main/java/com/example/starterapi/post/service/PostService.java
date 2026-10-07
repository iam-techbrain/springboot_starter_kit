package com.example.starterapi.post.service;

import com.example.starterapi.common.response.PagedResponse;
import com.example.starterapi.post.dto.CreatePostRequest;
import com.example.starterapi.post.dto.PostResponse;
import com.example.starterapi.post.dto.UpdatePostRequest;
import org.springframework.data.domain.Pageable;

/**
 * Service contract for Post domain operations.
 */
public interface PostService {

    PostResponse createPost(Long authorId, CreatePostRequest request);

    PostResponse getPostById(Long id);

    PostResponse getPostBySlug(String slug);

    PagedResponse<PostResponse> getAllPublishedPosts(String search, Pageable pageable);

    PagedResponse<PostResponse> getPostsByAuthor(Long authorId, Pageable pageable);

    PostResponse updatePost(Long id, Long currentUserId, boolean isAdmin, UpdatePostRequest request);

    void deletePost(Long id, Long currentUserId, boolean isAdmin);
}
