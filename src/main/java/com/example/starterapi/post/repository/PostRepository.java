package com.example.starterapi.post.repository;

import com.example.starterapi.post.entity.Post;
import com.example.starterapi.post.entity.PostStatus;
import com.example.starterapi.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndDeletedFalse(Long id);

    Optional<Post> findBySlugAndDeletedFalse(String slug);

    boolean existsBySlug(String slug);

    Page<Post> findAllByStatusAndDeletedFalse(PostStatus status, Pageable pageable);

    Page<Post> findAllByAuthorAndDeletedFalse(User author, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.deleted = false AND p.status = :status AND " +
            "(LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(CAST(p.content AS string)) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Post> searchPosts(@Param("status") PostStatus status, @Param("query") String query, Pageable pageable);
}
