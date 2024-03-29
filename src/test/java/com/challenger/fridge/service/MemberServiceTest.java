package com.challenger.fridge.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.challenger.fridge.common.StorageStatus;
import com.challenger.fridge.domain.Member;
import com.challenger.fridge.domain.Storage;
import com.challenger.fridge.dto.member.MemberInfoResponse;
import com.challenger.fridge.dto.box.response.StorageBoxNameResponse;
import com.challenger.fridge.dto.member.ChangePasswordRequest;
import com.challenger.fridge.dto.member.MemberNicknameRequest;
import com.challenger.fridge.dto.sign.SignUpRequest;
import com.challenger.fridge.dto.storage.request.StorageSaveRequest;
import com.challenger.fridge.repository.MemberRepository;
import com.challenger.fridge.repository.StorageRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    SignService signService;
    @Autowired
    StorageService storageService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    StorageRepository storageRepository;

    private static final String EMAIL = "springTest@test.com";
    private static final String PASSWORD = "1234";
    private static final String NAME = "springTest";

    private static final String EMAILWITHOUTSTORAGE = "noStorage@test.com";
    private static final String NAMEWITHOUTSTORAGE = "noStorage";

    private static final Long fridgeCount = 2L;
    private static final Long freezerCount = 3L;

    private Long mainStorageId;
    private Long subStorageId;
    private Long subStorageId2;

    @BeforeEach
    void setUp() {
        signService.registerMember(new SignUpRequest(EMAILWITHOUTSTORAGE, PASSWORD, NAMEWITHOUTSTORAGE));
        signService.registerMember(new SignUpRequest(EMAIL, PASSWORD, NAME));
        mainStorageId = storageService.saveStorage(new StorageSaveRequest("메인저장소", fridgeCount, freezerCount), EMAIL);
        subStorageId = storageService.saveStorage(new StorageSaveRequest("서브저장소", 1L, 1L), EMAIL);
        subStorageId2 = storageService.saveStorage(new StorageSaveRequest("두번째서브저장소", 1L, 1L), EMAIL);
    }

    @DisplayName("메인 보관소가 있는 회원 정보 조회")
    @Test
    void memberInfo() {
        String email = EMAIL;

        MemberInfoResponse memberInfo = memberService.findUserInfo(email);
        List<StorageBoxNameResponse> storageBoxes = memberInfo.getStorageBoxes();

        assertThat(memberInfo.getUsername()).isEqualTo(NAME);
        assertThat(memberInfo.getEmail()).isEqualTo(EMAIL);
        assertThat(memberInfo.getMainStorageId()).isEqualTo(mainStorageId);
        assertThat(memberInfo.getMainStorageName()).isEqualTo("메인저장소");

        assertThat(storageBoxes.size()).isEqualTo(fridgeCount + freezerCount);
        assertThat(storageBoxes.get(0).getStorageBoxName()).isEqualTo("냉장고1");
        assertThat(storageBoxes.get(1).getStorageBoxName()).isEqualTo("냉장고2");
        assertThat(storageBoxes.get(2).getStorageBoxName()).isEqualTo("냉동고1");
        assertThat(storageBoxes.get(3).getStorageBoxName()).isEqualTo("냉동고2");
        assertThat(storageBoxes.get(4).getStorageBoxName()).isEqualTo("냉동고3");
    }

    @DisplayName("메인 보관소가 없는 회원 정보 조회")
    @Test
    void memberInfoWithoutStorage() {
        String email = EMAILWITHOUTSTORAGE;

        MemberInfoResponse memberInfo = memberService.findUserInfo(email);

        assertThat(memberInfo.getUsername()).isEqualTo(NAMEWITHOUTSTORAGE);
        assertThat(memberInfo.getEmail()).isEqualTo(EMAILWITHOUTSTORAGE);
        assertThat(memberInfo.getMainStorageId()).isNull();
        assertThat(memberInfo.getMainStorageName()).isNull();
        assertThat(memberInfo.getStorageBoxes()).isNull();
    }

    @DisplayName("현재 비밀번호칸에 틀린 비밀번호 입력시 예외 발생")
    @Test
    void changeMemberInfoWithSamePassword() {
        String email = EMAIL;
        String currentPassword = "4321";
        String newPassword = "newPassword";

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(currentPassword, newPassword);

        assertThrows(IllegalArgumentException.class, () ->
                memberService.changeUserInfo(email, changePasswordRequest));
    }

    @DisplayName("새로운 비밀번호로 회원 정보 수정")
    @Test
    void changeMemberInfoWithNewPassword() {
        String email = EMAIL;
        String currentPassword = PASSWORD;
        String newPassword = "newPassword";
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(currentPassword, newPassword);
        
        memberService.changeUserInfo(email, changePasswordRequest);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(IllegalArgumentException::new);

        assertThat(encoder.matches(newPassword, member.getPassword())).isTrue();
    }

    @DisplayName("현재 닉네임과 같은 닉네임 입력시 예외 발생")
    @Test
    void changeToSameNickname() {
        String email = EMAIL;
        String newNickname = NAME;
        MemberNicknameRequest memberNicknameRequest = new MemberNicknameRequest(newNickname);

        assertThrows(IllegalArgumentException.class, () ->
                memberService.changeUserNickname(email, memberNicknameRequest));
    }

    @DisplayName("닉네임 변경 테스트")
    @Test
    void changeNickname() {
        String email = EMAIL;
        String newNickname = "newNickname";
        MemberNicknameRequest memberNicknameRequest = new MemberNicknameRequest(newNickname);

        Long memberId = memberService.changeUserNickname(email, memberNicknameRequest);
        Member memberWithNewNickname = memberRepository.findById(memberId)
                .orElseThrow(IllegalArgumentException::new);

        assertThat(memberWithNewNickname.getNickname()).isEqualTo(newNickname);
    }
}