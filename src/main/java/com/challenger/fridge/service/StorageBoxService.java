package com.challenger.fridge.service;

import com.challenger.fridge.domain.Item;
import com.challenger.fridge.domain.StorageItem;
import com.challenger.fridge.domain.box.StorageBox;
import com.challenger.fridge.dto.item.request.StorageItemRequest;
import com.challenger.fridge.exception.ItemNotFoundException;
import com.challenger.fridge.exception.StorageBoxNotFoundException;
import com.challenger.fridge.repository.ItemRepository;
import com.challenger.fridge.repository.StorageBoxRepository;
import com.challenger.fridge.repository.StorageItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StorageBoxService {
    private final StorageBoxRepository storageBoxRepository;
    private final ItemRepository itemRepository;
    private final StorageItemRepository storageItemRepository;

    public Long saveStorageItem(StorageItemRequest storageItemRequest, Long storageBoxId) {
        StorageBox storageBox = storageBoxRepository.findById(storageBoxId).orElseThrow(() -> new StorageBoxNotFoundException("해당 세부 보관소가 없습니다."));
        Item item = itemRepository.findById(storageItemRequest.getItemId()).orElseThrow(() -> new ItemNotFoundException("해당 하는 상품이 없습니다."));
        StorageItem storageItem = StorageItem.createStorageItem(storageItemRequest, item, storageBox);
        StorageItem savedStorageItem = storageItemRepository.save(storageItem);
        return savedStorageItem.getId();
    }


}
