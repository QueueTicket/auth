package com.qticket.auth.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Gateway에서 전달된 사용자 정보 헤더
        String userId = request.getHeader("X-USER-ID");
        String userRole = request.getHeader("X-USER-ROLE");

        if (userId != null && userRole != null) {
            // 인가(Authorization) 정보 설정
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + userRole);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userId, null, Collections.singletonList(authority));

            // SecurityContext에 인가 정보 설정
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // 다음 필터로 진행
        filterChain.doFilter(request, response);
    }
}