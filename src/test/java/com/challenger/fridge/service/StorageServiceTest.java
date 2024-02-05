package com.challenger.fridge.service;

import com.challenger.fridge.common.MemberRole;
import com.challenger.fridge.common.StorageStatus;
import com.challenger.fridge.domain.Member;
import com.challenger.fridge.domain.Storage;
import com.challenger.fridge.domain.box.StorageBox;
import com.challenger.fridge.dto.box.request.StorageBoxSaveRequest;
import com.challenger.fridge.dto.box.request.StorageMethod;
import com.challenger.fridge.dto.storage.request.StorageSaveRequest;
import com.challenger.fridge.exception.StorageBoxNameDuplicateException;
import com.challenger.fridge.exception.StorageNameDuplicateException;
import com.challenger.fridge.repository.MemberRepository;
import com.challenger.fridge.repository.StorageBoxRepository;
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
    @Mock
    StorageBoxRepository storageBoxRepository;


    @Test
    @DisplayName("보관소 추가를 할 때 보관소의 이름이 중복될 때")
    void 보관소이름중복예외() {
        // given
        StorageSaveRequest storageSaveRequest = createStorageSaveRequest("테스트냉장고", 1L, 1L, 1L);
        String userEmail = "123@naver.com";
        Member testMember = createTestMember(1L);
        Storage testStorage = createTestStorage("테스트냉장고", StorageStatus.NORMAL, testMember);
        //보관소 중복 검사를 위해 양방향에서 해당 회원의 보관소리스트에서 이름을 찾기 때문에 주입해줘야한다.
        testMember.getStorageList().add(testStorage);
        when(memberRepository.findByEmail(userEmail)).thenReturn(Optional.of(testMember));

        assertThrows(StorageNameDuplicateException.class, () -> {
            storageService.saveStorage(storageSaveRequest, userEmail);
        });

        verify(storageRepository, never()).save(any(Storage.class));
    }

    @Test
    @DisplayName("세부 보관소 저장을 할 때 세부 보관소의 이름이 하나라도 중복되는게 있다면 예외")
    void 세부보관소이름예외() {
        //given
        StorageBoxSaveRequest storageBoxSaveRequest = createStorageBoxSaveRequest("테스트세부보관소", StorageMethod.FRIDGE);
        Long storageId = 1L;
        Member testMember = createTestMember(1L);
        Storage testStorage = createTestStorage("테스트보관소", StorageStatus.NORMAL, testMember);
        StorageBox testStorageBox = createTestStorageBox("테스트세부보관소", StorageMethod.FRIDGE, testStorage);
        testStorage.getStorageBoxList().add(testStorageBox);
        //when
        when(storageRepository.findById(anyLong())).thenReturn(Optional.of(testStorage));
        //then
        assertThrows(StorageBoxNameDuplicateException.class, () -> {
            storageService.saveStorageBox(storageBoxSaveRequest, storageId);
        });

        verify(storageBoxRepository, never()).save(any(StorageBox.class));
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

    private Storage createTestStorage(String storageName, StorageStatus storageStatus, Member member) {
        Storage storage = new Storage(storageName, storageStatus, member);
        return storage;
    }

    private StorageBox createTestStorageBox(String storageBoxName, StorageMethod storageMethod, Storage storage) {
        StorageBox storageBox = StorageBox.createStorageBox(storageBoxName, storageMethod, storage);
        return storageBox;
    }

    private StorageSaveRequest createStorageSaveRequest(String storageName, Long freezeCount, Long roomCount, Long fridgeCount) {
        StorageSaveRequest storageSaveRequest = new StorageSaveRequest();
        storageSaveRequest.setStorageName(storageName);
        storageSaveRequest.setFreezeCount(freezeCount);
        storageSaveRequest.setFridgeCount(fridgeCount);
        return storageSaveRequest;
    }

    private StorageBoxSaveRequest createStorageBoxSaveRequest(String storageBoxName, StorageMethod storageMethod) {
        StorageBoxSaveRequest storageBoxSaveRequest = new StorageBoxSaveRequest();
        storageBoxSaveRequest.setStorageBoxName(storageBoxName);
        storageBoxSaveRequest.setStorageMethod(storageMethod);
        return storageBoxSaveRequest;
    }
}
