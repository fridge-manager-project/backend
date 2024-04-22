package com.challenger.fridge.dto.storage.response;

import com.challenger.fridge.common.StorageStatus;
import com.challenger.fridge.domain.Storage;
import com.challenger.fridge.dto.box.response.StorageBoxResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Schema(description = "보관소 정보 Response")
public class StorageResponse {
    @Schema(description = "보관소 고유 ID")
    private Long storageId;
    @Schema(description = "보관소 고유 타입")
    private StorageStatus storageStatus;
    @Schema(description = "보관소 이름")
    private String storageName;
    @Schema(description = "보관소 이미지 번호")
    private Long storageImage;
    @Schema(description = "보관소 내 세부 보관소 정보 리스트")
    private List<StorageBoxResponse> storageBoxes;

    public StorageResponse(Storage storage) {
        this.storageId = storage.getId();
        this.storageStatus = storage.getStatus();
        this.storageName = storage.getName();
        this.storageImage = storage.getStorageImage();
        this.storageBoxes = storage.getStorageBoxList().stream()
                .map(storageBox -> StorageBoxResponse.createStorageBoxResponse(storageBox))
                .collect(Collectors.toList());
    }

}
