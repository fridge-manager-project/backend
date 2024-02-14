package com.challenger.fridge.controller;

import com.challenger.fridge.dto.ApiResponse;
import com.challenger.fridge.dto.box.request.StorageBoxSaveRequest;
import com.challenger.fridge.dto.box.request.StorageBoxUpdateRequest;
import com.challenger.fridge.dto.box.response.StorageBoxResponse;
import com.challenger.fridge.dto.storage.request.StorageSaveRequest;
import com.challenger.fridge.dto.storage.response.StorageResponse;
import com.challenger.fridge.repository.StorageBoxRepository;
import com.challenger.fridge.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/storage")
@Tag(name = "storage", description = "보관소 API")
public class StorageController {
    private final StorageService storageService;
    private final StorageBoxRepository storageBoxRepository;

    @PostMapping
    @Operation(summary = "보관소 추가", description = "보관소를 추가한다(사용자가 현재 보관소가 하나라도 존재하면 NORMAL로 없으면 회원가입이라고 인식후 MAIN으로 만듬")
    public ApiResponse createStorage(@Valid @RequestBody StorageSaveRequest storageSaveRequest
            , @AuthenticationPrincipal User user) {
        String userEmail = user.getUsername();
        storageService.saveStorage(storageSaveRequest, userEmail);
        return ApiResponse.success(null);
    }

    @GetMapping
    @Operation(summary = "보관소 전체 조회", description = "보관소들의 정보들을 전체 조회한다.")
    public ApiResponse getStorage(@AuthenticationPrincipal User user) {
        String userEmail = user.getUsername();
        List<StorageResponse> storageList = storageService.findStorage(userEmail);
        return ApiResponse.success(storageList);
    }

    @PostMapping("/{storageId}/storagebox/new")
    @Operation(summary = "세부 보관소 추가", description = "세부 보관소를 추가한다(사용자가 FRIDGE,FREEZE 둘 중 하나를 선택해서 추가 할 수 있다.")
    public ApiResponse createStorageBox(@Valid @RequestBody StorageBoxSaveRequest storageBoxSaveRequest
            , @PathVariable Long storageId) {
        storageService.saveStorageBox(storageBoxSaveRequest, storageId);
        return ApiResponse.success(null);
    }

    @PatchMapping("{storageId}/storagebox/{storageBoxId}")
    @Operation(summary = "세부 보관소 수정", description = "세부 보관소 이름을 수정한다.")
    public ApiResponse modifyStorageBox(@RequestBody StorageBoxUpdateRequest storageBoxUpdateRequest
                                        ,@PathVariable Long storageBoxId
                                        ,@PathVariable Long storageId) {
        //현재 수정해야할 필드(세부 보관소 이름) 하나이고 만약에 Patch메소드라 자원을 수정하지 않고 넘어오면 null값이 들어오게 된다 그렇다면 그냥 기존의 storageBox 정보를 넘겨준다.

        if (storageBoxUpdateRequest.getStorageBoxName()==null)
        {
            return ApiResponse.success(storageBoxRepository.findById(storageBoxId).stream().map(storageBox -> StorageBoxResponse.createStorageBoxResponse(storageBox)));
        }
        storageService.updateStorageBox(storageBoxUpdateRequest, storageBoxId,storageId);
        return ApiResponse.success(storageBoxRepository.findById(storageBoxId).stream().map(storageBox -> StorageBoxResponse.createStorageBoxResponse(storageBox)));
    }

    @DeleteMapping("{storageId}/storagebox/{storageBoxId}")
    @Operation(summary = "세부 보관소 삭제", description = "세부 보관소를 삭제한다(storageItem들도 모두 삭제 된다.")
    public ApiResponse cancelStorageBox(@PathVariable Long storageId
                                        ,@PathVariable Long storageBoxId)
    {
        storageService.deleteStorageBox(storageBoxId,storageId);

        return ApiResponse.success(null);
    }



}
