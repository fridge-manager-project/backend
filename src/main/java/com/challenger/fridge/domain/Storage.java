package com.challenger.fridge.domain;

import static jakarta.persistence.FetchType.*;

import com.challenger.fridge.common.StorageStatus;
import com.challenger.fridge.domain.box.StorageBox;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Storage {

    @Id @GeneratedValue
    @Column(name = "storage_id")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private StorageStatus status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "storage",cascade = CascadeType.ALL)
    private List<StorageBox> storageBoxList = new ArrayList<>();

    public Storage(String name, StorageStatus status, Member member) {
        this.name = name;
        this.status = status;
        this.member = member;
    }

    //보관소 이름 중복 테스트용
    public Storage(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addStorageBox(StorageBox storageBox)
    {
        storageBoxList.add(storageBox);
        storageBox.setStorage(this);
    }

    public static Storage createStorage(String storageName, List<StorageBox> storageBoxList,Member member)
    {
        StorageStatus storageStatus = StorageStatus.NORMAL;
        if (member.getStorageList().isEmpty())
        {
            storageStatus = StorageStatus.MAIN;
        }
        Storage storage=new Storage(storageName,storageStatus,member);
        for (StorageBox storageBox: storageBoxList)
        {
            storage.addStorageBox(storageBox);
        }
        return storage;
    }


}
