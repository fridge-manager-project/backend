package com.challenger.fridge.dto.sign;

import lombok.Getter;

@Getter
public class SignUpResponse {

    String name;

    public SignUpResponse(String name) {
        this.name = name;
    }
}
