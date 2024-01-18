package com.challenger.fridge.security;

import com.challenger.fridge.domain.Member;
import com.challenger.fridge.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username).orElseThrow
                (() -> new UsernameNotFoundException("이메일과 비밀번호가 일치하지 않습니다."));
        log.debug("loadUserByUsername user : {}", member);

        return new CustomUserDetails(member);
    }

}
