package com.challenger.fridge.dto.box.response;

import com.challenger.fridge.domain.box.StorageBox;
import com.challenger.fridge.dto.item.response.CategoryStorageItemResponse;
import com.challenger.fridge.dto.storage.response.StorageResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Schema(description = "세부 보관소 정보 Response")
@JsonInclude(JsonInclude.Include.NON_NULL) //null인 데이터는 포함하지 않음
public class StorageBoxResponse {
    @Schema(description = "세부 보관소 고유 ID")
    private Long storageBoxId;
    @Schema(description = "세부 보관소 이름")
    private String storageBoxName;
    @Schema(description = "세부 보관소 타입")
    private String storageBoxType;
    @Schema(description = "세부 보관소 내에 있는 카테고리별 상품 리스트")
    private List<CategoryStorageItemResponse> categoriesItems;

    public static StorageBoxResponse createStorageBoxResponse(StorageBox storageBox)
    {
       StorageBoxResponse storageBoxResponse=new StorageBoxResponse();
       storageBoxResponse.setStorageBoxId(storageBox.getId());
       storageBoxResponse.setStorageBoxName(storageBox.getName());
       storageBoxResponse.setStorageBoxType(storageBox.getDtype());
       return storageBoxResponse;
    }

    public static StorageBoxResponse createStorageBoxDetailResponse(StorageBox storageBox,List<CategoryStorageItemResponse> categoriesItems)
    {
        StorageBoxResponse storageBoxResponse=new StorageBoxResponse();
        storageBoxResponse.setStorageBoxId(storageBox.getId());
        storageBoxResponse.setStorageBoxName(storageBox.getName());
        storageBoxResponse.setStorageBoxType(storageBox.getDtype());
        storageBoxResponse.setCategoriesItems(categoriesItems);
        return storageBoxResponse;
    }


}
