package com.challenger.fridge.service;

import com.challenger.fridge.domain.Member;
import com.challenger.fridge.dto.sign.SignUpRequest;
import com.challenger.fridge.dto.sign.SignUpResponse;
import com.challenger.fridge.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SignService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder encoder;

    /**
     * 회원 이메일 중복 확인 요청
     */
    public boolean checkDuplicateEmail(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }
        return true;
    }

    @Transactional
    public SignUpResponse registerMember(SignUpRequest request) {
        Member member = memberRepository.save(Member.from(request, encoder));
        return new SignUpResponse(member.getName());
    }
}
