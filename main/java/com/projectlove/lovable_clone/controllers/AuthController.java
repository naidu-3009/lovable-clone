package com.projectlove.lovable_clone.controllers;


import com.projectlove.lovable_clone.Services.AuthService;
import com.projectlove.lovable_clone.Services.UserService;
import com.projectlove.lovable_clone.dto.auth.AuthResponse;
import com.projectlove.lovable_clone.dto.auth.LoginRequest;
import com.projectlove.lovable_clone.dto.auth.SignUpRequest;
import com.projectlove.lovable_clone.dto.auth.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {
    private AuthService authService;
    private UserService userService;


    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(SignUpRequest request){
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> signup(LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getProfile(){
        Long userId=1L;
        return ResponseEntity.ok(userService.getProfile(userId));
    }



}
