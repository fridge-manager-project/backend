package com.challenger.fridge.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.challenger.fridge.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SignServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private SignService signService;

    @DisplayName("중복 이메일 입력시 예외 발생")
    @Test
    void createDuplicateEmailException() {
        String email = "jjw1234@naver.com";
        when(memberRepository.existsByEmail(anyString())).thenReturn(true);

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

}