package com.challenger.fridge.service;

import com.challenger.fridge.domain.Item;
import com.challenger.fridge.domain.StorageItem;
import com.challenger.fridge.domain.box.StorageBox;
import com.challenger.fridge.dto.box.response.StorageBoxResponse;
import com.challenger.fridge.dto.item.request.StorageItemRequest;
import com.challenger.fridge.dto.item.request.StorageItemUpdateRequest;
import com.challenger.fridge.dto.item.response.CategoryStorageItemResponse;
import com.challenger.fridge.dto.item.response.StorageItemResponse;
import com.challenger.fridge.exception.ItemNotFoundException;
import com.challenger.fridge.exception.StorageBoxNotFoundException;
import com.challenger.fridge.exception.StorageItemNotFoundException;
import com.challenger.fridge.repository.ItemRepository;
import com.challenger.fridge.repository.StorageBoxRepository;
import com.challenger.fridge.repository.StorageItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StorageBoxService {
    private final StorageBoxRepository storageBoxRepository;
    private final ItemRepository itemRepository;
    private final StorageItemRepository storageItemRepository;

    @Transactional
    public Long saveStorageItem(StorageItemRequest storageItemRequest, Long storageBoxId) {
        StorageBox storageBox = storageBoxRepository.findById(storageBoxId).orElseThrow(() -> new StorageBoxNotFoundException("해당 세부 보관소가 없습니다."));
        Item item = itemRepository.findById(storageItemRequest.getItemId()).orElseThrow(() -> new ItemNotFoundException("해당 하는 상품이 없습니다."));
        StorageItem storageItem = StorageItem.createStorageItem(storageItemRequest, item, storageBox);
        StorageItem savedStorageItem = storageItemRepository.save(storageItem);
        return savedStorageItem.getId();
    }


    public StorageBoxResponse findStorageBox(List<String> categoriesName, Long storageBoxId) {
        StorageBox storageBox;

        if (categoriesName != null) {
            storageBox = storageBoxRepository.findStorageItemsByIdAndCategories(storageBoxId, categoriesName)
                    .orElseThrow(() -> new StorageBoxNotFoundException("해당 세부 보관소가 없습니다."));
        } else {
            storageBox = storageBoxRepository.findStorageItemsById(storageBoxId)
                    .orElseThrow(() -> new StorageBoxNotFoundException("해당 세부 보관소가 없습니다."));
        }

        Map<String, List<StorageItemResponse>> categoryStorageItemMap = storageBox.getStorageItemList().stream()
                .collect(Collectors.groupingBy(
                        storageItem -> storageItem.getItem().getCategory().getCategoryName(),
                        Collectors.mapping(storageItem -> new StorageItemResponse(storageItem), Collectors.toList())
                ));
        List<CategoryStorageItemResponse> categoryStorageItemList = categoryStorageItemMap.entrySet().stream()
                .map(storageItem -> new CategoryStorageItemResponse(storageItem.getKey(), storageItem.getValue()))
                .collect(Collectors.toList());
        StorageBoxResponse storageBoxResponse = StorageBoxResponse.createStorageBoxDetailResponse(storageBox, categoryStorageItemList);
        return storageBoxResponse;
    }

    @Transactional
    public void deleteStorageItem(Long storageItemId)
    {
        StorageItem storageItem = storageItemRepository.findById(storageItemId).orElseThrow(() -> new StorageItemNotFoundException("해당 하는 세부 보관소 상품이 없습니다"));
        storageItemRepository.delete(storageItem);
    }

    @Transactional
    public void updateStorageItem(StorageItemUpdateRequest storageItemUpdateRequest,Long storageItemId)
    {
        StorageItem storageItem = storageItemRepository.findById(storageItemId).orElseThrow(() -> new StorageItemNotFoundException("해당하는 세부 보관소 상품이 없습니다."));
        storageItem.changeStorageItem(storageItemUpdateRequest);
    }


}
