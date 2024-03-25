package com.challenger.fridge.dto.member;

import com.challenger.fridge.domain.Member;
import com.challenger.fridge.domain.Storage;
import com.challenger.fridge.dto.box.response.StorageBoxNameResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberInfoResponse {

    private String username;
    private String email;
    private Long mainStorageId;
    private String mainStorageName;
    private List<StorageBoxNameResponse> storageBoxes;

    public MemberInfoResponse(Member member) {
        this.username = member.getNickname();
        this.email = member.getEmail();
        Storage storage = member.getStorageList().get(0);
        this.mainStorageId = storage.getId();
        this.mainStorageName = storage.getName();
        this.storageBoxes = storage.getStorageBoxList().stream()
                .map(StorageBoxNameResponse::new)
                .collect(Collectors.toList());
    }

    public static MemberInfoResponse createInfoWithoutStorage(Member member) {
        return MemberInfoResponse.builder()
                .username(member.getNickname())
                .email(member.getEmail())
                .mainStorageId(null)
                .mainStorageName(null)
                .storageBoxes(null)
                .build();
    }
}
