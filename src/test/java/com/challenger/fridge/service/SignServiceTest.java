package com.challenger.fridge.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.challenger.fridge.repository.MemberRepository;
import com.challenger.fridge.security.JwtTokenProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class SignServiceTest {

    @Autowired SignService signService;
    @Autowired MemberRepository memberRepository;
    @Autowired PasswordEncoder encoder;
    @Autowired JwtTokenProvider jwtTokenProvider;
    @Autowired AuthenticationManagerBuilder authenticationManagerBuilder;

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

}