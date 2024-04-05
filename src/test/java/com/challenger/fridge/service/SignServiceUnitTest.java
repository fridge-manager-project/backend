package com.challenger.fridge.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.challenger.fridge.common.MemberRole;
import com.challenger.fridge.domain.Member;
import com.challenger.fridge.dto.sign.SignUpRequest;
import com.challenger.fridge.dto.sign.SignUpResponse;
import com.challenger.fridge.repository.MemberRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class SignServiceUnitTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private SignService signService;

    @DisplayName("중복 이메일 입력시 예외 발생")
    @Test
    void createDuplicateEmailException() {
        String email = "jjw1234@naver.com";
        when(memberRepository.existsByEmail(anyString()))
                .thenThrow(new IllegalArgumentException("이미 사용중인 이메일입니다."));

        assertThatThrownBy(() -> signService.checkDuplicateEmail(email))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 사용중인 이메일입니다.");
    }

    @DisplayName("중복되지 않은 이메일 입력")
    @Test
    void createAdequateEmail() {
        String email = "jjw1234@naver.com";
        when(memberRepository.existsByEmail(anyString())).thenReturn(false);

        assertThat(signService.checkDuplicateEmail(email)).isTrue();
    }

    @DisplayName("회원가입")
    @Test
    void join() {
        Long memberId = 1L;
        Member testMember = createTestMember(memberId);
        SignUpRequest request = new SignUpRequest("jjw@naver.com", "1234", "jjw");

        SignUpResponse signUpResponse = new SignUpResponse("jjw");
        when(memberRepository.save(any())).thenReturn(testMember);

        assertThat(signService.registerMember(request).getName()).isEqualTo(signUpResponse.getName());
    }

    private Member createTestMember(Long memberId) {
        return Member.builder()
                .id(memberId)
                .email("jjw@naver.com")
                .password("1234")
                .nickname("jjw")
                .role(MemberRole.ROLE_USER)
//                .createdAt(LocalDateTime.now())
                .build();
    }

}