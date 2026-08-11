package com.projectlove.lovable_clone.security;

import com.projectlove.lovable_clone.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Component
public class AuthUtil {


    @Value("${jwt.secret-key}")
    private  String jwtSecretKey;

    public String generateAccessToken(User user){
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("userId",user.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*60*10))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    public SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public JwtUserPrincipal verifyAccessToken(String token){
        Claims claims=Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
        Long userId = Long.parseLong(claims.get("userId", String.class));

        String userName=claims.getSubject();
        return new JwtUserPrincipal(userId.toString(),userName,new ArrayList<>());
    }

    public Long getCurrentUserId(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication ==null || !(authentication.getPrincipal() instanceof  JwtUserPrincipal)){
            throw  new AuthenticationCredentialsNotFoundException("No JWT Found");
        }
        return Long.parseLong(((JwtUserPrincipal) authentication.getPrincipal()).userId());

    }


}
