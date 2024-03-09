package com.challenger.fridge.dto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberNicknameRequest {

    @NotBlank(message = "새로운 닉네임을 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "닉네임은 영어랑 숫자만 가능합니다.")
    private String nickname;
}
