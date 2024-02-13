package com.challenger.fridge.security;

import com.challenger.fridge.exception.TokenNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String[] allowedUrl = {"/", "/sign-in", "/sign-up", "/swagger-ui/**", "/v3/**"};

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("JwtAuthenticationFilter 통과");

        // 토큰이 없을 때 허용된 url 인 경우 다음 필터 진행
        if (Arrays.asList(allowedUrl).contains(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = resolveToken(request);
        try {
            if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                log.info("SecurityContext 에 인증객체 저장");
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Save authentication in SecurityContextHolder.");
            } else {
                throw new TokenNotFoundException("토큰이 없습니다. 다시 시도해주세요");
            }
        } catch (Exception e) {
            log.info("JwtFilter - doFilterInternal() 오류 발생");
            log.info("예외 : {}", e.getClass());
            request.setAttribute("exception", e);
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
