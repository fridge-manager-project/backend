package com.challenger.fridge.service;

import com.challenger.fridge.common.StorageStatus;
import com.challenger.fridge.domain.Cart;
import com.challenger.fridge.domain.Member;
import com.challenger.fridge.dto.member.MemberInfoResponse;
import com.challenger.fridge.dto.member.ChangePasswordRequest;
import com.challenger.fridge.dto.member.MemberNicknameRequest;
import com.challenger.fridge.repository.FCMTokenRepository;
import com.challenger.fridge.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final FCMTokenRepository fcmTokenRepository;

    public MemberInfoResponse findUserInfo(String email) {
        Member member = memberRepository.findMemberStorageByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        if (member.getStorageList().isEmpty()) {
            return MemberInfoResponse.createInfoWithoutStorage(member);
        }
        return new MemberInfoResponse(member);
    }

    @Transactional
    public void changeUserInfo(String email, ChangePasswordRequest changePasswordRequest) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        member.changePassword(changePasswordRequest, passwordEncoder);
    }

    @Transactional
    public Long changeUserNickname(String email, MemberNicknameRequest memberNicknameRequest) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        member.changeNickname(memberNicknameRequest);
        return member.getId();
    }

    @Transactional
    public void changeNotificationReception(String deviceToken, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        if(StringUtils.hasText(deviceToken)) {
            fcmTokenRepository.deleteFCMToken(email);
            fcmTokenRepository.saveFCMToken(email, deviceToken);
            member.receiveNotification();
            return;
        }
        member.preventNotification();
    }

}
