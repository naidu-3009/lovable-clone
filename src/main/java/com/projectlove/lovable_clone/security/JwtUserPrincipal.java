package com.projectlove.lovable_clone.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;


public record JwtUserPrincipal(
        String userId,
        String userName,
        List<GrantedAuthority> authorities
) {

}
