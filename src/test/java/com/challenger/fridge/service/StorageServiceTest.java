package com.challenger.fridge.service;

import com.challenger.fridge.common.MemberRole;
import com.challenger.fridge.domain.Member;
import com.challenger.fridge.domain.Storage;
import com.challenger.fridge.dto.storage.request.StorageSaveRequest;
import com.challenger.fridge.exception.StorageBoxLimitExceededException;
import com.challenger.fridge.exception.StorageNameDuplicateException;
import com.challenger.fridge.repository.MemberRepository;
import com.challenger.fridge.repository.StorageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StorageServiceTest {
    @InjectMocks
    StorageService storageService;
    @Mock
    StorageRepository storageRepository;
    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("보관소 추가를 할 때 보관소의 이름이 중복될 때")
    void 보관소이름중복예외() {
        // given
        StorageSaveRequest storageSaveRequest = getStorageSaveRequest("테스트냉장고", 1L, 1L, 1L);
        String userEmail = "123@naver.com";
        Member testMember = createTestMember(1L);
        Storage testStorage = createTestStorage(1L, "테스트냉장고");
        //보관소 중복 검사를 위해 양방향에서 해당 회원의 보관소리스트에서 이름을 찾기 때문에 주입해줘야한다.
        testMember.getStorageList().add(testStorage);
        when(memberRepository.findByEmail(userEmail)).thenReturn(Optional.of(testMember));

        assertThrows(StorageNameDuplicateException.class, () -> {
            storageService.saveStorage(storageSaveRequest, userEmail);
        });

        verify(storageRepository, never()).save(any(Storage.class));
    }

    @Test
    @DisplayName("보관소 추가를 할 때 세부 보관소의 합이 10개 이상 일 때")
    void 세부보관소의합이10개이상일떄예외() {
        // given
        StorageSaveRequest storageSaveRequest = getStorageSaveRequest("테스트냉장고", 10L, 15L, 1L);
        String userEmail = "123@naver.com";
        Member testMember = createTestMember(1L);
        when(memberRepository.findByEmail(userEmail)).thenReturn(Optional.of(testMember));

        assertThrows(StorageBoxLimitExceededException.class, () -> {
            storageService.saveStorage(storageSaveRequest, userEmail);
        });

        verify(storageRepository, never()).save(any(Storage.class));
    }

    private Member createTestMember(Long memberId) {
        return Member.builder()
                .id(memberId)
                .email("jjw@naver.com")
                .password("1234")
                .name("jjw")
                .role(MemberRole.ROLE_USER)
                .storageList(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .build();
    }

    //보관소 이름 중복만을 위해 필요한 필드만 주입
    private Storage createTestStorage(Long memberId, String storageName) {
        Storage storage = new Storage(1L, "테스트냉장고");
        return storage;
    }

    private StorageSaveRequest getStorageSaveRequest(String storageName, Long freezeCount, Long roomCount, Long fridgeCount) {
        StorageSaveRequest storageSaveRequest = new StorageSaveRequest();
        storageSaveRequest.setStorageName(storageName);
        storageSaveRequest.setFreezeCount(freezeCount);
        storageSaveRequest.setRoomCount(roomCount);
        storageSaveRequest.setFridgeCount(fridgeCount);
        return storageSaveRequest;
    }


}
