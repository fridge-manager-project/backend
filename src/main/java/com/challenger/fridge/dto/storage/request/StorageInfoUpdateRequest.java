package com.challenger.fridge.dto.storage.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StorageInfoUpdateRequest {
    private String storageName;
    private Long storageImage;
}
