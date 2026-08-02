package com.projectlove.lovable_clone.Services.impl;

import com.projectlove.lovable_clone.Services.AuthService;
import com.projectlove.lovable_clone.dto.auth.AuthResponse;
import com.projectlove.lovable_clone.dto.auth.LoginRequest;
import com.projectlove.lovable_clone.dto.auth.SignUpRequest;
import org.springframework.stereotype.Service;


@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public AuthResponse signup(SignUpRequest request) {
        return null;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        return null;
    }
}
