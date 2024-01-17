package com.challenger.fridge.service;


import com.challenger.fridge.domain.Item;
import com.challenger.fridge.domain.Member;
import com.challenger.fridge.domain.Storage;
import com.challenger.fridge.domain.StorageItem;
import com.challenger.fridge.dto.storage.request.StorageItemRequest;
import com.challenger.fridge.dto.storage.response.CategoryItemResponse;
import com.challenger.fridge.dto.storage.response.StorageItemResponse;
import com.challenger.fridge.dto.storage.request.StorageRequest;
import com.challenger.fridge.dto.storage.response.StorageResponse;
import com.challenger.fridge.repository.ItemRepository;
import com.challenger.fridge.repository.MemberRepository;
import com.challenger.fridge.repository.StorageItemRepository;
import com.challenger.fridge.repository.StorageRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StorageService {
    private final StorageRepository storageRepository;
    private final MemberRepository memberRepository;
    private final StorageItemRepository storageItemRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Storage saveStorage(StorageRequest storageRequest, String userEmail) {
        Member member = memberRepository.findMemberByEmail(userEmail).orElseThrow(() -> new RuntimeException("현재 이메일을 가진 회원이 없습니다."));
        Storage saveStorage = Storage.createStorage(storageRequest, member);
        Storage savedStorage = storageRepository.save(saveStorage);
        return savedStorage;
    }

    @Transactional
    public StorageItem saveStorageItem(StorageItemRequest storageItemRequest, Long storageId) {
        Storage storage = storageRepository.findById(storageId).orElseThrow(() -> new RuntimeException("냉장고가 없습니다."));
        Item item = itemRepository.findById(storageItemRequest.getItemId()).orElseThrow(() -> new RuntimeException("해당 상품이 없습니다."));
        StorageItem storageItem = StorageItem.createStorageItem(storageItemRequest, item);
        storageItem.addStorageItem(storage);
        StorageItem savedStorageItem = storageItemRepository.save(storageItem);
        return savedStorageItem;
    }

    @Transactional
    public void deleteStorageItem(Long storageId, Long storageItemId) {
        StorageItem storageItem = storageItemRepository.findById(storageItemId).orElseThrow(() -> new RuntimeException("냉장고에 해당 상품이 들어 있지 않습니다."));
        storageItemRepository.delete(storageItem);
    }

    public StorageResponse findStorageItemLists(Long storageId) {
        List<Storage> findStorageItemList = storageRepository.findByStorageItemList(storageId);
        Long storageItemCount = findStorageItemList.stream().count();
        Map<String, List<StorageItem>> categoryItemsMap = findStorageItemList.stream()
                .flatMap(storage -> storage.getStorageItemList().stream())
                .collect(Collectors.groupingBy(
                        item -> item.getItem().getCategory().getCategoryName(),
                        Collectors.toList()
                ));
        // 카테고리별로 CategoryItemResponse를 만들어 리스트에 추가
        List<CategoryItemResponse> categoryItemResponses = categoryItemsMap.entrySet().stream()
                .map(entry -> {
                    String categoryName = entry.getKey();
                    List<StorageItem> itemsInCategory = entry.getValue();
                    List<StorageItemResponse> storageItemResponses = itemsInCategory.stream()
                            .map(item -> new StorageItemResponse(
                                    item.getId(),
                                    item.getItem().getId(),
                                    item.getItem().getItemName(),
                                    item.getQuantity(),
                                    item.getExpirationDate()
                            ))
                            .collect(Collectors.toList());
                    CategoryItemResponse categoryItemResponse = new CategoryItemResponse();
                    categoryItemResponse.setCategoryName(categoryName);
                    categoryItemResponse.setStorageItems(storageItemResponses);
                    return categoryItemResponse;
                })
                .collect(Collectors.toList());
        StorageResponse storageResponse = new StorageResponse(
                findStorageItemList.get(0).getMethod(),
                storageItemCount,
                categoryItemResponses
        );
        return storageResponse;
    }
}
