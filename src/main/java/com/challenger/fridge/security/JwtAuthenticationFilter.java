package com.challenger.fridge.security;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter {

    private final JwtTokenProvider jwtTokenProvider;


}
