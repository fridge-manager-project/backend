package com.challenger.fridge.domain.box;

import com.challenger.fridge.domain.Storage;
import com.challenger.fridge.domain.StorageItem;
import com.challenger.fridge.dto.storage.request.StorageSaveRequest;
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

}
