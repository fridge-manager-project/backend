package com.challenger.fridge.dto.storage.response;

import com.challenger.fridge.domain.Storage;

import lombok.Data;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CategoryStorageItemResponse {


    private String categoryName;
    private List<StorageItemResponse> storageItems;

    public CategoryStorageItemResponse(Storage storage) {
        this.storageItems = storage.getStorageItemList().stream()
                .map(storageItem -> new StorageItemResponse(storageItem))
                .collect(Collectors.toList());
    }
}
