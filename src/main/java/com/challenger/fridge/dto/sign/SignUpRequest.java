package com.challenger.fridge.dto.sign;

import lombok.Getter;

@Getter
public class SignUpRequest {

    private String email;
    private String password;
    private String name;
}
