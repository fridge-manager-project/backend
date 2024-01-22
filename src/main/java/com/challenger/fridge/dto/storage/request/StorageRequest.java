package com.challenger.fridge.dto.storage.request;

import com.challenger.fridge.common.StorageMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorageRequest {
    private String storageName;
    private StorageMethod storageMethod;
}
