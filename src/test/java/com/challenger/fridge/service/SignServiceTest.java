package com.challenger.fridge.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.challenger.fridge.domain.Member;
import com.challenger.fridge.dto.sign.SignInRequest;
import com.challenger.fridge.dto.sign.SignInResponse;
import com.challenger.fridge.dto.sign.SignUpRequest;
import com.challenger.fridge.repository.MemberRepository;
import com.challenger.fridge.security.JwtTokenProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class SignServiceTest {

    @Autowired
    SignService signService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    AuthenticationManagerBuilder authenticationManagerBuilder;

    @DisplayName("사용중인 이메일 입력 시 예외 발생")
    @Test
    void createDuplicateEmailException() {
        String email = "jjw@test.com";
        assertThatThrownBy(() -> signService.checkDuplicateEmail(email))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 사용중인 이메일입니다.");
    }

    @DisplayName("사용중이지 않은 이메일 입력")
    @Test
    void failByDuplicateEmail() {
        String email = "cjw@test.com";
        assertThat(signService.checkDuplicateEmail(email)).isTrue();
    }

    @DisplayName("등록되지 않은 이메일로 로그인")
    @Test
    void signInWithWrongEmail() {
        String wrongEmail = "ccc@test.com";
        String password = "1234";
        SignInRequest request = new SignInRequest(wrongEmail, password);

        assertThrows(BadCredentialsException.class, () -> signService.signIn(request));
    }

    @DisplayName("틀린 비밀번호로 로그인")
    @Test
    void signInWithWrongPassword() {
        String email = "jjw@test.com";
        String wrongPassword = "12345";
        SignInRequest request = new SignInRequest(email, wrongPassword);

        assertThrows(BadCredentialsException.class, () -> signService.signIn(request));
        assertThrows(AuthenticationException.class, () -> signService.signIn(request));
    }

    @DisplayName("등록된 이메일과 비밀번호로 로그인 - 성공")
    @Test
    void signIn() {
        //given
        String email = "jjw@test.com";
        String password = "1234";
        String name = "jjw";
        SignInRequest signInRequest = new SignInRequest(email, password);

        //when
        SignUpRequest signUpRequest = new SignUpRequest(email, password, name);
        Member member = memberRepository.findByEmail(email)
                .orElseGet(() -> memberRepository.save(Member.from(signUpRequest, encoder)));
        SignInResponse response = signService.signIn(signInRequest);

        //then
        assertThat(response.getName()).isEqualTo(member.getName());
    }
}