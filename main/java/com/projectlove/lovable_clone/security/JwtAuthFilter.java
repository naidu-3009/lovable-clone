package com.projectlove.lovable_clone.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    @Autowired
    private  AuthUtil authUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            log.info("incoming request {}",request.getRequestURI());
        SecurityContextHolder securityContextHolder;
      final String requestHeaderToken=request.getHeader("Authorization");

           if(requestHeaderToken==null || !(requestHeaderToken.startsWith("Bearer"))){
               filterChain.doFilter(request,response);
               return;
           }
       String token=requestHeaderToken.split("Bearer ")[1];//THe total header "Bearer header.payload.sign is split into 2 parts and here we are taking the 2nd part"
        JwtUserPrincipal userPrincipal=authUtil.verifyAccessToken(token);

        if(userPrincipal!=null && SecurityContextHolder.getContext().getAuthentication() ==null){
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new UsernamePasswordAuthenticationToken(userPrincipal,null, userPrincipal.authorities());
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }

        filterChain.doFilter(request,response);


    }
}
