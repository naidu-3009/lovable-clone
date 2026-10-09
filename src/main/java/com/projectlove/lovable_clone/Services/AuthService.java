package com.projectlove.lovable_clone.Services;

import com.projectlove.lovable_clone.dto.auth.AuthResponse;
import com.projectlove.lovable_clone.dto.auth.LoginRequest;
import com.projectlove.lovable_clone.dto.auth.SignUpRequest;

public interface AuthService {
    AuthResponse signup(SignUpRequest request);

    AuthResponse login(LoginRequest request);
}
