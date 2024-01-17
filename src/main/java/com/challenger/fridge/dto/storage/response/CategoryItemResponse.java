package com.challenger.fridge.dto.storage.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class CategoryItemResponse {
    private String categoryName;
    private List<StorageItemResponse> storageItems = new ArrayList<>();
}
