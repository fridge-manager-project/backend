package com.challenger.fridge.security;

import com.challenger.fridge.config.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String accessToken = resolveToken(request);
        String email = jwtTokenProvider.getAuthentication(accessToken).getName();
        if (redisService.hasKey("RT:" + email)) {
            redisService.deleteValues("RT:" + email);
        } else {
            log.info("해당 이메일 {} 을 가진 RefreshToken 은 존재하지 않습니다.", email);
        }
        redisService.setValuesWithTimeout("BlackList:" + accessToken, "logout",
                jwtTokenProvider.getTokenExpirationTime(accessToken));
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
