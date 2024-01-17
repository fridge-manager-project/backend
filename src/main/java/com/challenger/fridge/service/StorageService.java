package com.challenger.fridge.service;


import com.challenger.fridge.domain.Member;
import com.challenger.fridge.domain.Storage;
import com.challenger.fridge.domain.StorageItem;
import com.challenger.fridge.dto.CategoryItemResponse;
import com.challenger.fridge.dto.StorageItemResponse;
import com.challenger.fridge.dto.StorageRequest;
import com.challenger.fridge.dto.StorageResponse;
import com.challenger.fridge.repository.MemberRepository;
import com.challenger.fridge.repository.StorageRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StorageService {
    private final StorageRepository storageRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Storage saveStorage(StorageRequest storageRequest, String userEmail) {
        Member member = memberRepository.findMemberByEmail(userEmail).orElseThrow(() -> new RuntimeException("현재 이메일을 가진 회원이 없습니다."));
        Storage saveStorage = Storage.createStorage(storageRequest, member);
        Storage savedStorage = storageRepository.save(saveStorage);
        return savedStorage;
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
