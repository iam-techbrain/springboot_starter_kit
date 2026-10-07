package com.example.starterapi.user.service;

import com.example.starterapi.common.exception.ResourceNotFoundException;
import com.example.starterapi.common.response.PagedResponse;
import com.example.starterapi.user.dto.UpdateProfileRequest;
import com.example.starterapi.user.dto.UserProfileResponse;
import com.example.starterapi.user.entity.User;
import com.example.starterapi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of UserService demonstrating Spring Data JPA,
 * Streams API, and transaction management.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(Long userId) {
        User user = findUserById(userId);
        return UserProfileResponse.from(user);
    }

    @Override
    @Transactional
    public UserProfileResponse updateUserProfile(Long userId, UpdateProfileRequest request) {
        User user = findUserById(userId);
        user.setName(request.name());
        User updated = userRepository.save(user);
        return UserProfileResponse.from(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public PagedResponse<UserProfileResponse> getAllUsers(Pageable pageable) {
        Page<User> page = userRepository.findAllByDeletedFalse(pageable);
        Page<UserProfileResponse> dtoPage = page.map(UserProfileResponse::from);
        return PagedResponse.from(dtoPage);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }
}
