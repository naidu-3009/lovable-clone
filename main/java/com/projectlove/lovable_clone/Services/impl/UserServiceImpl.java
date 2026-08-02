package com.projectlove.lovable_clone.Services.impl;

import com.projectlove.lovable_clone.Services.UserService;
import com.projectlove.lovable_clone.dto.auth.UserProfileResponse;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public UserProfileResponse getProfile(Long userId) {
        return null;
    }
}
