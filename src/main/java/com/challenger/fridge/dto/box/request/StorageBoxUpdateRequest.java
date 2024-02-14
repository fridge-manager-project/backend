package com.challenger.fridge.dto.box.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "세부보관소 수정 DTO")
public class StorageBoxUpdateRequest {
    @Schema(description = "변경할 세부 보관소 이름")
    private String storageBoxName;
}
