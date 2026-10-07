package com.example.starterapi.post.service;

import com.example.starterapi.common.exception.ForbiddenException;
import com.example.starterapi.common.exception.ResourceNotFoundException;
import com.example.starterapi.common.response.PagedResponse;
import com.example.starterapi.post.dto.CreatePostRequest;
import com.example.starterapi.post.dto.PostResponse;
import com.example.starterapi.post.dto.UpdatePostRequest;
import com.example.starterapi.post.entity.Post;
import com.example.starterapi.post.entity.PostStatus;
import com.example.starterapi.post.repository.PostRepository;
import com.example.starterapi.user.entity.User;
import com.example.starterapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Locale;
import java.util.UUID;

/**
 * Implementation of PostService.
 * Enforces business rules, author-level authorization, slug generation,
 * view count tracking, and soft-delete semantics.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public PostResponse createPost(Long authorId, CreatePostRequest request) {
        User author = userRepository.findById(authorId)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", authorId));

        String slug = generateUniqueSlug(request.title());
        PostStatus status = request.status() != null ? request.status() : PostStatus.PUBLISHED;

        Post post = Post.builder()
                .title(request.title())
                .slug(slug)
                .content(request.content())
                .status(status)
                .author(author)
                .viewsCount(0L)
                .build();

        Post savedPost = postRepository.save(post);
        log.info("Post created with ID: {} and slug: {}", savedPost.getId(), savedPost.getSlug());

        return PostResponse.from(savedPost);
    }

    @Override
    @Transactional
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        post.incrementViews();
        postRepository.save(post);

        return PostResponse.from(post);
    }

    @Override
    @Transactional
    public PostResponse getPostBySlug(String slug) {
        Post post = postRepository.findBySlugAndDeletedFalse(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "slug", slug));

        post.incrementViews();
        postRepository.save(post);

        return PostResponse.from(post);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<PostResponse> getAllPublishedPosts(String search, Pageable pageable) {
        Page<Post> postsPage;
        if (StringUtils.hasText(search)) {
            postsPage = postRepository.searchPosts(PostStatus.PUBLISHED, search.trim(), pageable);
        } else {
            postsPage = postRepository.findAllByStatusAndDeletedFalse(PostStatus.PUBLISHED, pageable);
        }

        Page<PostResponse> dtoPage = postsPage.map(PostResponse::from);
        return PagedResponse.from(dtoPage);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<PostResponse> getPostsByAuthor(Long authorId, Pageable pageable) {
        User author = userRepository.findById(authorId)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", authorId));

        Page<Post> postsPage = postRepository.findAllByAuthorAndDeletedFalse(author, pageable);
        Page<PostResponse> dtoPage = postsPage.map(PostResponse::from);
        return PagedResponse.from(dtoPage);
    }

    @Override
    @Transactional
    public PostResponse updatePost(Long id, Long currentUserId, boolean isAdmin, UpdatePostRequest request) {
        Post post = postRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        assertOwnershipOrAdmin(post, currentUserId, isAdmin);

        post.setTitle(request.title());
        post.setContent(request.content());
        if (request.status() != null) {
            post.setStatus(request.status());
        }

        Post updatedPost = postRepository.save(post);
        log.info("Post updated with ID: {}", updatedPost.getId());

        return PostResponse.from(updatedPost);
    }

    @Override
    @Transactional
    public void deletePost(Long id, Long currentUserId, boolean isAdmin) {
        Post post = postRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

        assertOwnershipOrAdmin(post, currentUserId, isAdmin);

        post.markDeleted(); // Soft delete pattern
        postRepository.save(post);
        log.info("Post soft-deleted with ID: {}", id);
    }

    private void assertOwnershipOrAdmin(Post post, Long currentUserId, boolean isAdmin) {
        boolean isOwner = post.getAuthor().getId().equals(currentUserId);
        if (!isOwner && !isAdmin) {
            throw new ForbiddenException("You do not have permission to modify or delete this post");
        }
    }

    private String generateUniqueSlug(String title) {
        String baseSlug = title.toLowerCase(Locale.ENGLISH)
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");

        if (!StringUtils.hasText(baseSlug)) {
            baseSlug = "post";
        }

        String candidateSlug = baseSlug;
        int suffix = 1;
        while (postRepository.existsBySlug(candidateSlug)) {
            candidateSlug = baseSlug + "-" + suffix++;
            if (suffix > 50) {
                candidateSlug = baseSlug + "-" + UUID.randomUUID().toString().substring(0, 8);
                break;
            }
        }
        return candidateSlug;
    }
}
