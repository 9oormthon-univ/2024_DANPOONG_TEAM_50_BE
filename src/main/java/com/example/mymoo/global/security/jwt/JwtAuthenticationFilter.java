package com.example.mymoo.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        String uri = request.getRequestURI();
        String authorizationHeader = request.getHeader("Authorization");
        log.info("Request URI: {}, Authorization Header: {}", uri, authorizationHeader);

        // jwt 헤더가 없는 경우 다음 필터로
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authorizationHeader.substring(7); // 토큰 부분만 추출
        try {
            Claims claims = jwtTokenProvider.getClaimsFromToken(jwt);
            // Authentication 객체를 등록
            SecurityContextHolder.getContext().setAuthentication(
                jwtTokenProvider.getAuthenticationFromClaims(claims)
            );
        } catch (JwtException e) {
            log.error("JWT 토큰 오류: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", HttpServletResponse.SC_UNAUTHORIZED);
            errorResponse.put("message", e.getMessage());

            String jsonResponse = mapper.writeValueAsString(errorResponse);
            response.getWriter().write(jsonResponse);
            return;
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/v1/auth/login") ||
            path.startsWith("/api/v1/accounts/signup") ||
            path.startsWith("/api/v1/children/signup") ||
            path.startsWith("/api/v1/auth/token/refresh") ||
            path.startsWith("/api/v1/oauth/") ||
            path.equals("/") ||
            path.startsWith("/swagger-ui/") ||
            path.startsWith("/v3/api-docs/");
    }
}