package com.challenger.fridge.domain;

import static jakarta.persistence.FetchType.*;

import com.challenger.fridge.common.StorageStatus;
import com.challenger.fridge.domain.box.Freeze;
import com.challenger.fridge.domain.box.Fridge;
import com.challenger.fridge.domain.box.StorageBox;
import com.challenger.fridge.dto.box.request.StorageMethod;
import com.challenger.fridge.exception.StorageBoxLimitExceededException;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "storage_id")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private StorageStatus status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "storage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StorageBox> storageBoxList = new ArrayList<>();

    public Storage(String name, StorageStatus status, Member member) {
        this.name = name;
        this.status = status;
        this.member = member;
    }

    public void addStorageBox(StorageBox storageBox) {
        storageBoxList.add(storageBox);
        storageBox.setStorage(this);
    }

    public static Storage createStorage(String storageName, List<StorageBox> storageBoxList, Member member) {
        StorageStatus storageStatus = StorageStatus.NORMAL;
        if (member.getStorageList().isEmpty()) {
            storageStatus = StorageStatus.MAIN;
        }
        Storage storage = new Storage(storageName, storageStatus, member);
        for (StorageBox storageBox : storageBoxList) {
            storage.addStorageBox(storageBox);
        }
        return storage;
    }

    //현재 보관소에서 냉장고의 개수만 반환
    public Long getStorageBoxFridgeCount() {
        return storageBoxList.stream()
                .filter(storageBox -> storageBox instanceof Fridge)
                .count();
    }

    //현재 보관소에서 냉동고의 개수만 반환
    public Long getStorageBoxFreezeCount() {
        return storageBoxList.stream()
                .filter(storageBox -> storageBox instanceof Freeze)
                .count();
    }

    //해당 세부 보관소 개수 검사 ? : 사용 --> 현재 코드를 두가지 선택지여서 사용 후에 추가 사항있으면 if문
    //으로 변경 가능성 있음
    public void checkStorageBoxCount(StorageMethod storageMethod) {
        Long boxCount = storageMethod == StorageMethod.FRIDGE ? getStorageBoxFridgeCount() : getStorageBoxFreezeCount();
        if (boxCount > 10)
        {
            throw new StorageBoxLimitExceededException(storageMethod+" 의 개수가 초과하였습니다.");
        }

    }
}


