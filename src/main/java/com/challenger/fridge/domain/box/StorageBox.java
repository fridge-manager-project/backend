package com.challenger.fridge.domain.box;

import com.challenger.fridge.domain.Storage;
import com.challenger.fridge.domain.StorageItem;
import com.challenger.fridge.dto.storage.request.StorageSaveRequest;
import com.challenger.fridge.exception.StorageBoxLimitExceededException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class StorageBox {
    @Id
    @GeneratedValue
    @Column(name = "storage_box_id")
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "storage_id")
    private Storage storage;

    @OneToMany(mappedBy = "storageBox")
    private List<StorageItem> storageItemList=new ArrayList<>();

    public StorageBox(String name) {
        this.name = name;
    }

    public void setStorage(Storage storage)
    {
        this.storage=storage;
    }
    public static List<StorageBox> createStorageBox(StorageSaveRequest storageSaveRequest)
    {
        Long roomCount = storageSaveRequest.getRoomCount();
        Long fridgeCount = storageSaveRequest.getFridgeCount();
        Long freezeCount = storageSaveRequest.getFreezeCount();
        Long sum=roomCount+freezeCount+fridgeCount;
        if (sum >= 10)
        {
            throw new StorageBoxLimitExceededException("세부 보관소를 총합 10개 이상 생성할 수 없습니다.");
        }
        ArrayList<StorageBox> storageBoxes = new ArrayList<>();
        for (int i = 1; i <= storageSaveRequest.getFridgeCount(); i++) {
            Fridge fridge = Fridge.createFridge("냉장고" + i);
            storageBoxes.add(fridge);
        }
        for (int i = 1; i <= storageSaveRequest.getFreezeCount(); i++) {
            Freeze freeze = Freeze.createFridge("냉동고" + i);
            storageBoxes.add(freeze);
        }
        for (int i = 1; i <= storageSaveRequest.getRoomCount(); i++) {
            Room room = Room.createFridge("실온" + i);
            storageBoxes.add(room);
        }
        return storageBoxes;
    }

}
