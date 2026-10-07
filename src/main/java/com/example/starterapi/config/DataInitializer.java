package com.example.starterapi.config;

import com.example.starterapi.post.entity.Post;
import com.example.starterapi.post.entity.PostStatus;
import com.example.starterapi.post.repository.PostRepository;
import com.example.starterapi.user.entity.Role;
import com.example.starterapi.user.entity.User;
import com.example.starterapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Initializes default demo data (Admin user and sample post) on application startup.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByEmail("admin@example.com")) {
            User admin = User.builder()
                    .name("System Administrator")
                    .username("admin")
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ROLE_ADMIN)
                    .verified(true)
                    .enabled(true)
                    .build();

            User savedAdmin = userRepository.save(admin);
            log.info("Initialized default administrator account: admin@example.com");

            if (!postRepository.existsBySlug("welcome-to-spring-boot-starter-api")) {
                Post samplePost = Post.builder()
                        .title("Welcome to Spring Boot Starter API")
                        .slug("welcome-to-spring-boot-starter-api")
                        .content("This is an enterprise-grade REST API architecture built using Spring Boot 3, Spring Security 6, JWT, and JPA.")
                        .status(PostStatus.PUBLISHED)
                        .author(savedAdmin)
                        .viewsCount(1)
                        .build();

                postRepository.save(samplePost);
                log.info("Initialized sample blog post.");
            }
        }
    }
}
