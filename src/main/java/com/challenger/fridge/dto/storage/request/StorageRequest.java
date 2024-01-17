package com.challenger.fridge.dto.storage.request;

import com.challenger.fridge.common.StorageMethod;
import lombok.Data;

@Data
public class StorageRequest {
    private String storageName;
    private StorageMethod storageMethod;
}
