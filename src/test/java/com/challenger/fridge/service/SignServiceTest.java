package com.challenger.fridge.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.challenger.fridge.domain.Cart;
import com.challenger.fridge.domain.Member;
import com.challenger.fridge.dto.sign.SignInRequest;
import com.challenger.fridge.dto.sign.SignInResponse;
import com.challenger.fridge.dto.sign.SignUpRequest;
import com.challenger.fridge.dto.sign.SignUpResponse;
import com.challenger.fridge.dto.sign.TokenInfo;
import com.challenger.fridge.redis.RedisContainerTest;
import com.challenger.fridge.repository.CartRepository;
import com.challenger.fridge.repository.MemberRepository;
import com.challenger.fridge.security.JwtTokenProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class SignServiceTest extends RedisContainerTest {

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
    @Autowired
    CartRepository cartRepository;

    @BeforeEach
    void setUp() {
        String registeredEmail = "jjw@test.com";
        String password = "1234";
        String name = "jjw";
        signService.registerMember(new SignUpRequest(registeredEmail, password, name));
    }

    @DisplayName("사용중인 이메일 입력 시 예외 발생")
    @Test
    void createDuplicateEmailException() {
        String registeredEmail = "jjw@test.com";
        assertThatThrownBy(() -> signService.checkDuplicateEmail(registeredEmail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 사용중인 이메일입니다.");
    }

    @DisplayName("사용중이지 않은 이메일 입력")
    @Test
    void failByDuplicateEmail() {
        String unregisteredEmail = "cjw@test.com";
        assertThat(signService.checkDuplicateEmail(unregisteredEmail)).isTrue();
    }

    @DisplayName("등록되지 않은 이메일로 로그인")
    @Test
    void signInWithWrongEmail() {
        String unregisteredEmail = "cjw@test.com";
        String password = "1234";
        SignInRequest request = new SignInRequest(unregisteredEmail, password);

        assertThrows(BadCredentialsException.class, () -> signService.signIn(request, "deviceToken"));
    }

    @DisplayName("틀린 비밀번호로 로그인")
    @Test
    void signInWithWrongPassword() {
        String registeredEmail = "jjw@test.com";
        String wrongPassword = "12345";
        SignInRequest request = new SignInRequest(registeredEmail, wrongPassword);

        assertThrows(BadCredentialsException.class, () -> signService.signIn(request, "deviceToken"));
        assertThrows(AuthenticationException.class, () -> signService.signIn(request, "deviceToken"));
    }

    @DisplayName("등록된 이메일과 비밀번호로 로그인 - 성공")
    @Test
    void signIn() {
        //given
        String email = "jjw@test.com";
        String password = "1234";

        //when
        TokenInfo tokenInfo = signService.signIn(new SignInRequest(email, password), "deviceToken");
        Authentication authentication = jwtTokenProvider.getAuthentication(tokenInfo.getAccessToken());

        //then
        assertThat(authentication.getName()).isEqualTo(email);
    }

    @DisplayName("회원가입시 장바구니 생성 테스트")
    @Test
    void signUp() {
        // given
        String email = "hhh@test.com";
        String password = "1234";
        String name = "hhh";
        SignUpRequest request = new SignUpRequest(email, password, name);

        // when
        SignUpResponse signUpResponse = signService.registerMember(request);
        Cart cart = cartRepository.findByMemberEmail(email)
                .orElseThrow(IllegalArgumentException::new);
        Member member = cart.getMember();

        // then
        assertThat(cart).isNotNull();
        assertThat(member.getEmail()).isEqualTo(email);
        assertThat(member.getNickname()).isEqualTo(name);
    }
}