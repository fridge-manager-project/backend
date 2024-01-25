package com.challenger.fridge.dto;

import com.challenger.fridge.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberInfoDto {

    private String username;
    private String email;
//    private String password; // 암호화 풀지 말지 고민


    public MemberInfoDto(Member member) {
        username = member.getName();
        email = member.getEmail();
//        password = member.getPassword();
    }
}
