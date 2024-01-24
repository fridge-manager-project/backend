package com.challenger.fridge.dto.storage.response;

import com.challenger.fridge.common.StorageMethod;
import lombok.Data;

import java.util.List;

@Data
public class StorageResponse {
    private StorageMethod storageMethod;
    private int storageItemCount;
    private List<CategoryStorageItemResponse> categoriesItems;


    public StorageResponse(List<CategoryStorageItemResponse> categoryStorageItemResponse) {
        this.categoriesItems = categoryStorageItemResponse;

    }
}
