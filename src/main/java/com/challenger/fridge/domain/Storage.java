package com.challenger.fridge.domain;

import static jakarta.persistence.FetchType.*;

import com.challenger.fridge.common.StorageMethod;
import com.challenger.fridge.common.StorageStatus;
import com.challenger.fridge.dto.StorageRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private StorageMethod method;

    @Enumerated(EnumType.STRING)
    private StorageStatus status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "storage")
    private List<StorageItem> storageItemList=new ArrayList<>();

    private Storage(String name, StorageMethod method, StorageStatus status, Member member) {
        this.name = name;
        this.method = method;
        this.status = status;
        this.member = member;
    }

    public static Storage createStorage(StorageRequest storageRequest,Member member)
    {

        Storage storage=new Storage(storageRequest.getStorageName(),
                                    storageRequest.getStorageMethod(),
                                    StorageStatus.NORMAL,
                                    member);
        return storage;
    }
}
