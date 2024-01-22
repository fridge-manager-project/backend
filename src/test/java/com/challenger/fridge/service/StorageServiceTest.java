package com.challenger.fridge.service;

import com.challenger.fridge.common.MemberRole;
import com.challenger.fridge.common.StorageMethod;
import com.challenger.fridge.common.StorageStatus;
import com.challenger.fridge.domain.*;
import com.challenger.fridge.dto.storage.request.StorageItemRequest;
import com.challenger.fridge.dto.storage.request.StorageRequest;
import com.challenger.fridge.repository.ItemRepository;
import com.challenger.fridge.repository.MemberRepository;
import com.challenger.fridge.repository.StorageItemRepository;
import com.challenger.fridge.repository.StorageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class StorageServiceTest {
    @Mock
    StorageRepository storageRepository;

    @Mock
    StorageItemRepository storageItemRepository;

    @Mock
    MemberRepository memberRepository;

    @Mock
    ItemRepository itemRepository;
    @InjectMocks
    StorageService storageService;

    @Test
    @DisplayName("냉장고 추가 테스트")
    public void 냉장고추가() throws Exception {
        // Given
        StorageRequest storageRequest = new StorageRequest("심현석냉장고", StorageMethod.FRIDGE);
        Member testMember = createTestMember(1L);
        Storage storage = Storage.createStorage(storageRequest, testMember);
        when(memberRepository.findByEmail(testMember.getEmail())).thenReturn(Optional.of(testMember));
        when(storageRepository.save(any(Storage.class))).thenReturn(storage);
        // When
        Storage savedStorage = storageService.saveStorage(storageRequest, testMember.getEmail());

        assertThat(savedStorage.getName()).isEqualTo(storage.getName());
        assertThat(savedStorage.getMethod()).isEqualTo(storage.getMethod());

    }

    @Test
    @DisplayName("보관소로 상품 추가 테스트")
    public void 보관소에상품추가() throws Exception {

        Item testItem = createTestItem(1L);
        Storage testStorage = createTestStorage(1L);
        StorageItem testStorageItem = createTestStorageItem(1L);
        when(storageRepository.findById(anyLong())).thenReturn(Optional.of(testStorage));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(testItem));
        when(storageItemRepository.save(any(StorageItem.class))).thenReturn(testStorageItem);


        StorageItemRequest storageItemRequest = new StorageItemRequest(
                1L, "testItem", 3L,
                "2024-01-27T12:34:56", "2024-01-27T12:34:56");
        StorageItem savedStorageItem = storageService.saveStorageItem(storageItemRequest, testStorage.getId());

        assertThat(testStorageItem.getId()).isEqualTo(savedStorageItem.getId());
    }

    @Test
    @DisplayName("보관소 안에 있는 상품 단건 삭제")
    public void 보관소상품삭제() throws Exception {
        StorageItem testStorageItem = createTestStorageItem(1L);
        when(storageItemRepository.findById(1L)).thenReturn(Optional.of(testStorageItem));
        storageService.deleteStorageItem(testStorageItem.getId());
        verify(storageItemRepository).delete(testStorageItem);

    }
    private StorageItem createTestStorageItem(Long storageItemId)
    {
        Item testItem = createTestItem(1L);
        Storage testStorage = createTestStorage(1L);

        StorageItem storageItem = StorageItem.builder()
                .id(storageItemId)
                .storage(testStorage)
                .item(testItem)
                .quantity(3L)
                .purchaseDate(LocalDateTime.now())
                .expirationDate(LocalDateTime.now())
                .build();
        return storageItem;
    }
    private Item createTestItem(Long itemId) {
        Item testItem = Item.builder()
                .itemName("testItem")
                .id(1L)
                .build();
        return testItem;
    }

    private Storage createTestStorage(Long storageId) {

        Member testMember = createTestMember(1L);
        Storage testStorage = Storage.builder()
                .id(storageId)
                .member(testMember)
                .method(StorageMethod.FRIDGE)
                .name("테스트냉장고")
                .status(StorageStatus.NORMAL)
                .build();
        return testStorage;
    }

    private Member createTestMember(Long memberId) {
        return Member.builder()
                .id(memberId)
                .email("shs@naver.com")
                .password("1234")
                .name("shs")
                .role(MemberRole.ROLE_USER)
                .createdAt(LocalDateTime.now())
                .build();
    }


}
