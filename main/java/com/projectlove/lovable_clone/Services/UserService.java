package com.projectlove.lovable_clone.Services;

import com.projectlove.lovable_clone.dto.auth.UserProfileResponse;

public interface UserService {
    UserProfileResponse getProfile(Long userId);
}
