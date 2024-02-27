package com.challenger.fridge.dto.box.response;

import com.challenger.fridge.domain.box.StorageBox;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StorageBoxNameResponse {

    private Long storageBoxId;
    private String storageBoxName;

    public StorageBoxNameResponse(StorageBox storageBox) {
        this.storageBoxId = storageBox.getId();
        this.storageBoxName = storageBox.getName();
    }
}
