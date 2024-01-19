package com.challenger.fridge.dto.sign;

import com.challenger.fridge.common.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignInResponse {

    private String name;
    private TokenInfo tokenInfo;

}
