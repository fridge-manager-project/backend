package com.challenger.fridge.dto;

import com.challenger.fridge.common.StorageMethod;
import lombok.Data;

@Data
public class StorageRequest {
    private String storageName;
    private StorageMethod storageMethod;
}
