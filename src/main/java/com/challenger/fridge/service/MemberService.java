package com.challenger.fridge.service;

import com.challenger.fridge.common.StorageStatus;
import com.challenger.fridge.domain.Member;
import com.challenger.fridge.domain.Storage;
import com.challenger.fridge.dto.MemberInfoDto;
import com.challenger.fridge.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberInfoDto findUserInfo(String email) {
        Member member = memberRepository.findMemberStorageByEmail(email, StorageStatus.MAIN)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return new MemberInfoDto(member);
    }
}
