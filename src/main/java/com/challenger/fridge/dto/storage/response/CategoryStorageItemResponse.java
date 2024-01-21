package com.challenger.fridge.dto.storage.response;

import com.challenger.fridge.domain.Storage;

import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class CategoryStorageItemResponse {

    private String categoryName;
    private List<StorageItemResponse> storageItems;

    public CategoryStorageItemResponse(String categoryName ,List<StorageItemResponse> storageItems ) {
        this.categoryName=  categoryName;
        this.storageItems = storageItems;
    }
}
