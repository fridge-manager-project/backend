package com.challenger.fridge.service;

import com.challenger.fridge.common.StorageStatus;
import com.challenger.fridge.domain.Member;
import com.challenger.fridge.domain.Storage;
import com.challenger.fridge.dto.MemberInfoDto;
import com.challenger.fridge.dto.member.MemberInfoRequest;
import com.challenger.fridge.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberInfoDto findUserInfo(String email) {
        Member member = memberRepository.findMemberStorageByEmail(email, StorageStatus.MAIN)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return new MemberInfoDto(member);
    }

    @Transactional
    public void changeUserInfo(String email, MemberInfoRequest memberInfoRequest) {
        Member member = memberRepository.findMemberAndStorageByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Storage newMainStorage = member.getStorageList().stream()
                .filter(storage -> storage.getId().equals(memberInfoRequest.getMainStorageId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("바꾸려는 저장소를 찾을 수 없습니다. 다시 시도해주세요"));

        member.changeInfo(newMainStorage, memberInfoRequest.getPassword(), passwordEncoder);
    }
}
