package com.example.starterapi.post.controller;

import com.example.starterapi.common.response.ApiResponse;
import com.example.starterapi.common.response.PagedResponse;
import com.example.starterapi.post.dto.CreatePostRequest;
import com.example.starterapi.post.dto.PostResponse;
import com.example.starterapi.post.dto.UpdatePostRequest;
import com.example.starterapi.post.service.PostService;
import com.example.starterapi.security.CurrentUser;
import com.example.starterapi.security.service.CustomUserDetails;
import com.example.starterapi.user.entity.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller exposing endpoints for Post resource management.
 */
@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Tag(name = "Posts", description = "Endpoints for creating, reading, updating, and soft-deleting posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Create a new post (Authenticated)")
    public ResponseEntity<ApiResponse<PostResponse>> createPost(
            @CurrentUser CustomUserDetails currentUser,
            @Valid @RequestBody CreatePostRequest request) {
        PostResponse response = postService.createPost(currentUser.getId(), request);
        return new ResponseEntity<>(ApiResponse.success("Post created successfully", response), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get paginated published posts with optional search filter")
    public ResponseEntity<ApiResponse<PagedResponse<PostResponse>>> getAllPosts(
            @RequestParam(name = "search", required = false) String search,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PagedResponse<PostResponse> response = postService.getAllPublishedPosts(search, pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get post details by ID")
    public ResponseEntity<ApiResponse<PostResponse>> getPostById(@PathVariable("id") Long id) {
        PostResponse response = postService.getPostById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get post details by unique URL slug")
    public ResponseEntity<ApiResponse<PostResponse>> getPostBySlug(@PathVariable("slug") String slug) {
        PostResponse response = postService.getPostBySlug(slug);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/my-posts")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Get posts written by currently logged-in user")
    public ResponseEntity<ApiResponse<PagedResponse<PostResponse>>> getMyPosts(
            @CurrentUser CustomUserDetails currentUser,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        PagedResponse<PostResponse> response = postService.getPostsByAuthor(currentUser.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Update an existing post (Author or Admin only)")
    public ResponseEntity<ApiResponse<PostResponse>> updatePost(
            @PathVariable("id") Long id,
            @CurrentUser CustomUserDetails currentUser,
            @Valid @RequestBody UpdatePostRequest request) {
        boolean isAdmin = currentUser.getUser().getRole() == Role.ROLE_ADMIN;
        PostResponse response = postService.updatePost(id, currentUser.getId(), isAdmin, request);
        return ResponseEntity.ok(ApiResponse.success("Post updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "Soft delete an existing post (Author or Admin only)")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable("id") Long id,
            @CurrentUser CustomUserDetails currentUser) {
        boolean isAdmin = currentUser.getUser().getRole() == Role.ROLE_ADMIN;
        postService.deletePost(id, currentUser.getId(), isAdmin);
        return ResponseEntity.ok(ApiResponse.success("Post deleted successfully"));
    }
}
