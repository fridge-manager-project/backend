package com.challenger.fridge.dto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberNicknameRequest {

    @NotBlank(message = "새로운 닉네임을 입력해주세요.")
    @Size(min = 1, max = 8)
    private String nickname;
}
