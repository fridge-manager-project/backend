package com.challenger.fridge.service;

import com.challenger.fridge.domain.Member;
import com.challenger.fridge.dto.sign.SignInRequest;
import com.challenger.fridge.dto.sign.SignInResponse;
import com.challenger.fridge.dto.sign.SignUpRequest;
import com.challenger.fridge.dto.sign.SignUpResponse;
import com.challenger.fridge.repository.MemberRepository;
import com.challenger.fridge.security.JwtTokenProvider;
import com.challenger.fridge.dto.sign.TokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SignService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

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
        Member member = memberRepository.save(Member.from(request, encoder));
        return new SignUpResponse(member.getName());
    }

    /**
     * 로그인
     */
    @Transactional
    public SignInResponse signIn(SignInRequest request) {
        // 1. email, password 기반 Authentication 객체 생성. -> 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                request.getEmail(), request.getPassword());

        // 2. 검증 진행 - CustomUserDetailsService.loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. member 에 refresh token 저장 - 미완
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        return new SignInResponse(member.getName(), tokenInfo);
    }

}
