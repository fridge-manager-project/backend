package com.challenger.fridge.service;

import com.challenger.fridge.config.RedisService;
import com.challenger.fridge.domain.Cart;
import com.challenger.fridge.domain.Member;
import com.challenger.fridge.dto.sign.SignInRequest;
import com.challenger.fridge.dto.sign.SignInResponse;
import com.challenger.fridge.dto.sign.SignUpRequest;
import com.challenger.fridge.dto.sign.SignUpResponse;
import com.challenger.fridge.repository.FCMTokenRepository;
import com.challenger.fridge.repository.MemberRepository;
import com.challenger.fridge.security.JwtTokenProvider;
import com.challenger.fridge.dto.sign.TokenInfo;
import io.netty.util.internal.ObjectUtil;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SignService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    private final FCMTokenRepository fcmTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisService redisService;

    /**
     * 회원 이메일 중복 확인 요청
     */
    public boolean checkDuplicateEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }
        return true;
    }

    /**
     * 회원가입
     */
    @Transactional
    public SignUpResponse registerMember(SignUpRequest request) {
        Cart cart = Cart.createCart();
        Member member = memberRepository.save(Member.from(request, encoder, cart));
        return new SignUpResponse(member.getNickname());
    }

    /**
     * 로그인
     */
    @Transactional
    public TokenInfo signIn(SignInRequest signInRequest) {
        // 1. email, password 기반 Authentication 객체 생성. -> 인증 여부를 확인하는 authenticated 값이 false
        log.info("1. email, password 기반 Authentication 객체 생성.");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                signInRequest.getEmail(), signInRequest.getPassword());

        // 2. 검증 진행 - CustomUserDetailsService.loadUserByUsername 메서드가 실행
        log.info("2. 검증 진행 - CustomUserDetailsService.loadUserByUsername 메서드가 실행");
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. deviceToken 이 있다면 저장하고 알림 켜기, 없다면 그냥 두기
        String deviceToken = signInRequest.getDeviceToken();
        if (StringUtils.hasText(deviceToken)) {
            log.info("deviceToken={} 있음", deviceToken);
            Member member = memberRepository.findByEmail(signInRequest.getEmail())
                    .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
            member.receiveNotification();
            fcmTokenRepository.saveFCMToken(member.getEmail(), deviceToken);
        } else {
            log.info("deviceToken 없음.");
        }

        // 4. AT, RT 생성 및 Redis 에 RT 저장
        log.info("3. AT, RT 생성 및 Redis 에 RT 저장");
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        saveRefreshToken(authentication.getName(), tokenInfo.getRefreshToken());
        return tokenInfo;
    }

    @Transactional
    public void saveRefreshToken(String email, String refreshToken) {
        redisService.setValuesWithTimeout("RT:" + email, refreshToken,
                jwtTokenProvider.getTokenExpirationTime(refreshToken));
    }

    /**
     * 토큰 재발급 : validate() 가 true 반환할 때만 사용
     */
    @Transactional
    public TokenInfo reissue(String requestAccessTokenInHeader, String requestRefreshTokenInHeader) {
        String requestAccessToken = resolveToken(requestAccessTokenInHeader);

        Authentication authentication = jwtTokenProvider.getAuthentication(requestAccessToken);
        String principal = getPrincipal(requestAccessToken);

        String refreshTokenInRedis = redisService.getValues("RT:" + principal);

        // Redis 에 저장되어 있는 RT가 없을 경우 재로그인 요청
        if(refreshTokenInRedis == null) {
            return null;
        }

        // 요청된 RT의 유효성 검사 & Redis 에 저장되어 있는 RT와 같은지 비교
        log.info("requestRefreshTokenInHeader : {}", requestRefreshTokenInHeader);
        if (!jwtTokenProvider.validateRefreshToken(requestRefreshTokenInHeader) || !refreshTokenInRedis.equals(
                requestRefreshTokenInHeader)) {
            redisService.deleteValues("RT:" + principal);
            return null;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 토큰 재발급 및 Redis 에 RT 업데이트
        redisService.deleteValues("RT:" + principal);
        log.info("기존 RT 삭제");
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        saveRefreshToken(principal, tokenInfo.getRefreshToken());
        log.info("새로운 RT 저장");
        return tokenInfo;
    }


    public String getPrincipal(String requestAccessToken) {
        return jwtTokenProvider.getAuthentication(requestAccessToken).getName();
    }

    public String resolveToken(String requestAccessTokenInHeader) {
        if (requestAccessTokenInHeader != null && requestAccessTokenInHeader.startsWith("Bearer ")) {
            return requestAccessTokenInHeader.substring(7);
        }
        return null;
    }
}
