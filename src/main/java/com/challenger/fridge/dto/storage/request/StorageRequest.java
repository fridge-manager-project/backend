package com.challenger.fridge.dto.storage.request;

import com.challenger.fridge.common.StorageMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorageRequest {
    @NotBlank(message = "냉장고 이름은 필수로 입력해야 합니다.")
    private String storageName;
    @NotNull(message = "보관 방식은 필수로 입력해야 합니다.")
    private StorageMethod storageMethod;
}
