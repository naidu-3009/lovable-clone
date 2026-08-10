package com.projectlove.lovable_clone.Services.impl;

import com.projectlove.lovable_clone.Services.AuthService;
import com.projectlove.lovable_clone.dto.auth.AuthResponse;
import com.projectlove.lovable_clone.dto.auth.LoginRequest;
import com.projectlove.lovable_clone.dto.auth.SignUpRequest;
import com.projectlove.lovable_clone.entity.User;
import com.projectlove.lovable_clone.error.BadRequestException;
import com.projectlove.lovable_clone.mapper.UserMapper;
import com.projectlove.lovable_clone.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;


    @Override
    public AuthResponse signup(SignUpRequest request) {
        userRepository.findByUsername(request.username()).ifPresent(user -> {
            throw new BadRequestException("user already exists with user name:"+ request.username());
        });
        User user=userMapper.toUserEntity(request);
        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);

        return new AuthResponse("dummy",userMapper.toUserProfileResponse(user));

    }

    @Override
    public AuthResponse login(LoginRequest request) {
        return null;
    }
}
