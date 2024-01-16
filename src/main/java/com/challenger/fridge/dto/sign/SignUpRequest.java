package com.challenger.fridge.dto.sign;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpRequest {

    private String email;
    private String password;
    private String name;
}
