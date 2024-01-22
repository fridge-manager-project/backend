package com.challenger.fridge.service;

import com.challenger.fridge.common.MemberRole;
import com.challenger.fridge.common.StorageMethod;
import com.challenger.fridge.common.StorageStatus;
import com.challenger.fridge.domain.*;
import com.challenger.fridge.dto.storage.request.StorageItemRequest;
import com.challenger.fridge.dto.storage.request.StorageRequest;
import com.challenger.fridge.dto.storage.response.StorageItemDetailsResponse;
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

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


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
    public void 냉장고추가() {
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
    public void 보관소에상품추가() {
        Long storageId = 1L;
        Long storageItemId = 1L;
        Long itemId = 1L;
        Long categoryId = 1L;
        Item testItem = createTestItem(itemId, categoryId);
        Storage testStorage = createTestStorage(storageId);
        StorageItem testStorageItem = createTestStorageItem(storageItemId, itemId, categoryId);
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
    public void 보관소상품삭제() {
        Long storageId = 1L;
        Long storageItemId = 1L;
        Long itemId = 1L;
        Long categoryId = 1L;
        StorageItem testStorageItem = createTestStorageItem(storageItemId, itemId, categoryId);
        when(storageItemRepository.findById(storageItemId)).thenReturn(Optional.of(testStorageItem));
        storageService.deleteStorageItem(testStorageItem.getId());
        //메소드가 몇번 실행 됬는지 확인
        verify(storageItemRepository, times(1)).delete(testStorageItem);

    }

    @Test
    @DisplayName("보관소 안에 있는 상품 단건 조회")
    public void 보관소상품단건조회() {
        Long storageId = 1L;
        Long storageItemId = 1L;
        Long itemId = 1L;
        Long categoryId = 1L;
        StorageItem testStorageItem = createTestStorageItem(storageItemId, itemId, categoryId);
        when(storageItemRepository.findByStorageItemDetails(storageItemId)).thenReturn(Optional.of(testStorageItem));
        StorageItemDetailsResponse storageItemDetailsResponse = storageService.findStorageItem(storageId, storageItemId);
        assertThat(storageItemDetailsResponse.getStorageId()).isEqualTo(testStorageItem.getId());
    }

  /*  @Test
    @DisplayName("보관소 단건 냉장고 조회 (카테고리별 개수 테스트)")
    public void 보관소단건냉장고조회() {
        Long storageId = 1L;
        Storage testStorage = createTestStorage(1L);
        when(storageRepository.findByStorageItemList(storageId)).thenReturn(Arrays.asList(testStorage))

    }*/

    private StorageItem createTestStorageItem(Long storageItemId, Long itemId, Long categoryId) {
        Item testItem = createTestItem(itemId, categoryId);
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

    private Item createTestItem(Long itemId, Long categoryId) {
        Category testCategory = createTestCategory(categoryId);
        Item testItem = Item.builder()
                .itemName("testItem")
                .category(testCategory)
                .id(itemId)
                .build();
        return testItem;
    }

    private Category createTestCategory(Long categoryId) {
        Category testCategory = Category.builder()
                .categoryName("catest" + categoryId)
                .parentCategory(Category.builder()
                        .categoryName("parentCatest" + categoryId).build())
                .id(categoryId)
                .build();
        return testCategory;
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
