package com.example.starterapi.user.service;

import com.example.starterapi.common.response.PagedResponse;
import com.example.starterapi.user.dto.UpdateProfileRequest;
import com.example.starterapi.user.dto.UserProfileResponse;
import org.springframework.data.domain.Pageable;

/**
 * Service contract for user operations.
 */
public interface UserService {

    UserProfileResponse getUserProfile(Long userId);

    UserProfileResponse updateUserProfile(Long userId, UpdateProfileRequest request);

    PagedResponse<UserProfileResponse> getAllUsers(Pageable pageable);
}
