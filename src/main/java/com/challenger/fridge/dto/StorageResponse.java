package com.challenger.fridge.dto;

import com.challenger.fridge.common.StorageMethod;

import java.util.ArrayList;
import java.util.List;

public class StorageResponse {
    private StorageMethod storageMethod;
    private Long storageItemCount;
    private List<CategoryItemResponse> categoriesItems=new ArrayList<>();

    public StorageResponse(StorageMethod storageMethod, Long storageItemCount, List<CategoryItemResponse> categoriesItems) {
        this.storageMethod = storageMethod;
        this.storageItemCount = storageItemCount;
        this.categoriesItems = categoriesItems;

    }
}
