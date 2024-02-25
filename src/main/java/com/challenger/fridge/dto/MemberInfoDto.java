package com.challenger.fridge.dto;

import com.challenger.fridge.domain.Member;
import com.challenger.fridge.domain.Storage;
import com.challenger.fridge.dto.box.response.StorageBoxNameResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberInfoDto {

    private String username;
    private String email;
    private Long mainStorageId;
    private String mainStorageName;
    private List<StorageBoxNameResponse> storageBoxes;

    public MemberInfoDto(Member member) {
        this.username = member.getName();
        this.email = member.getEmail();
        Storage storage = member.getStorageList().get(0);
        this.mainStorageId = storage.getId();
        this.mainStorageName = storage.getName();
        this.storageBoxes = storage.getStorageBoxList().stream()
                .map(StorageBoxNameResponse::new)
                .collect(Collectors.toList());
    }
}
