package com.challenger.fridge.domain.box;

import com.challenger.fridge.domain.Storage;
import com.challenger.fridge.domain.StorageItem;
import com.challenger.fridge.dto.box.request.StorageBoxUpdateRequest;
import com.challenger.fridge.dto.box.request.StorageMethod;
import com.challenger.fridge.dto.storage.request.StorageSaveRequest;
import com.challenger.fridge.exception.StorageMethodMatchingException;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "storage_box_id")
    private Long id;

    private String name;

    @Column(name = "DTYPE", insertable = false, updatable = false)
    private String dtype;

    @ManyToOne
    @JoinColumn(name = "storage_id")
    private Storage storage;

    @OneToMany(mappedBy = "storageBox", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<StorageItem> storageItemList = new ArrayList<>();

    public StorageBox(String name) {
        this.name = name;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public static List<StorageBox> createStorageBox(StorageSaveRequest storageSaveRequest) {
        Long fridgeCount = storageSaveRequest.getFridgeCount();
        Long freezeCount = storageSaveRequest.getFreezeCount();
        ArrayList<StorageBox> storageBoxes = new ArrayList<>();
        for (int i = 1; i <= fridgeCount; i++) {
            Fridge fridge = Fridge.createFridge("냉장고" + i);
            storageBoxes.add(fridge);
        }
        for (int i = 1; i <= freezeCount; i++) {
            Freeze freeze = Freeze.createFridge("냉동고" + i);
            storageBoxes.add(freeze);
        }
        return storageBoxes;
    }

    public static StorageBox createStorageBox(String storageName, StorageMethod storageMethod, Storage storage) {
        //사실 유효성 검사에서 보관 방식이 잘못되면 걸러지지만 비즈니스 로직에서도 한번도 검사 하는 부분을 추가
        switch (storageMethod) {
            case FRIDGE:
                Fridge fridge = new Fridge(storageName);
                fridge.setStorage(storage);
                return fridge;
            case FREEZE:
                Freeze freeze = new Freeze(storageName);
                freeze.setStorage(storage);
                return freeze;
            default:
                throw new StorageMethodMatchingException("보관 저장 방식이 잘못되었습니다.");
        }
    }

    /**
     * storageBox 변경 로직
     *
     * @param storageBoxUpdateRequest
     */
    public void changeStorageBox(StorageBoxUpdateRequest storageBoxUpdateRequest) {
        this.name = storageBoxUpdateRequest.getStorageBoxName();
    }

}
