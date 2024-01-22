package com.challenger.fridge.service;


import com.challenger.fridge.common.StorageMethod;
import com.challenger.fridge.domain.Item;
import com.challenger.fridge.domain.Member;
import com.challenger.fridge.domain.Storage;
import com.challenger.fridge.domain.StorageItem;
import com.challenger.fridge.dto.storage.request.StorageItemRequest;
import com.challenger.fridge.dto.storage.response.CategoryStorageItemResponse;
import com.challenger.fridge.dto.storage.response.StorageItemDetailsResponse;
import com.challenger.fridge.dto.storage.request.StorageRequest;
import com.challenger.fridge.dto.storage.response.StorageItemResponse;
import com.challenger.fridge.dto.storage.response.StorageResponse;
import com.challenger.fridge.exception.ItemNotFoundException;
import com.challenger.fridge.exception.StorageItemNotFountException;
import com.challenger.fridge.exception.StorageNotFoundException;
import com.challenger.fridge.repository.ItemRepository;
import com.challenger.fridge.repository.MemberRepository;
import com.challenger.fridge.repository.StorageItemRepository;
import com.challenger.fridge.repository.StorageRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class StorageService {
    private final StorageRepository storageRepository;
    private final MemberRepository memberRepository;
    private final StorageItemRepository storageItemRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Storage saveStorage(StorageRequest storageRequest, String userEmail) {
        Member member = memberRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("현재 이메일을 가진 회원이 없습니다."));
        Storage saveStorage = Storage.createStorage(storageRequest, member);
        Storage savedStorage = storageRepository.save(saveStorage);
        return savedStorage;
    }

    @Transactional
    public StorageItem saveStorageItem(StorageItemRequest storageItemRequest, Long storageId) {
        Storage storage = storageRepository.findById(storageId).orElseThrow(() -> new StorageNotFoundException("냉장고가 없습니다."));
        Item item = itemRepository.findById(storageItemRequest.getItemId()).orElseThrow(() -> new ItemNotFoundException("해당 상품이 없습니다."));
        StorageItem storageItem = StorageItem.createStorageItem(storageItemRequest, item);
        storageItem.addStorageItem(storage);
        StorageItem savedStorageItem = storageItemRepository.save(storageItem);
        return savedStorageItem;
    }

    @Transactional
    public void deleteStorageItem(Long storageItemId) {
        StorageItem storageItem = storageItemRepository.findById(storageItemId).orElseThrow(() -> new StorageItemNotFountException("냉장고에 해당 상품이 들어 있지 않습니다."));
        storageItemRepository.delete(storageItem);
    }

    public StorageResponse findStorageItemLists(Long storageId) {
        List<Storage> findStorageItemList = storageRepository.findByStorageItemList(storageId);

        int storageItemCount = findStorageItemList.get(0).getStorageItemList().size();
        StorageMethod storageMethod = findStorageItemList.get(0).getMethod();

        Map<String, List<StorageItemResponse>> categoryStorageItemMap = findStorageItemList.stream()
                .flatMap(storage -> storage.getStorageItemList().stream())
                .collect(Collectors.groupingBy(
                        storageItem -> storageItem.getItem().getCategory().getCategoryName(),
                        Collectors.mapping(storageItem -> new StorageItemResponse(storageItem), Collectors.toList())
                ));
        List<CategoryStorageItemResponse> categoryStorageItemList = categoryStorageItemMap.entrySet().stream()
                .map(storageItem -> new CategoryStorageItemResponse(storageItem.getKey(), storageItem.getValue()))
                .collect(Collectors.toList());
        StorageResponse storageResponse = new StorageResponse(categoryStorageItemList);
        storageResponse.setStorageMethod(storageMethod);
        storageResponse.setStorageItemCount(storageItemCount);
        return storageResponse;
    }

    public StorageItemDetailsResponse findStorageItem(Long storageId, Long storageItemId) {
        StorageItem storageItem = storageItemRepository.findByStorageItemDetails(storageItemId).orElseThrow(() -> new StorageItemNotFountException("해당 하는 상품이 냉장고에 없습니다."));
        return new StorageItemDetailsResponse(storageItem.getStorage().getId(),
                storageItem.getStorage().getName(),
                storageItem.getId(),
                storageItem.getItem().getId(),
                storageItem.getItem().getItemName(),
                storageItem.getItem().getCategory().getCategoryName(),
                storageItem.getExpirationDate(),
                storageItem.getQuantity()
        );
    }
}