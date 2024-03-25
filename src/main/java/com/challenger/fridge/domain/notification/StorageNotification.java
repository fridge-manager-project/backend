package com.challenger.fridge.domain.notification;

import com.challenger.fridge.domain.Member;
import com.challenger.fridge.domain.StorageItem;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DiscriminatorValue(value = "storage")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StorageNotification extends Notification {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storage_item_id")
    private StorageItem storageItem;

    public StorageNotification(Member member, StorageItem storageItem) {
        super(member);
        this.storageItem = storageItem;
    }

    public StorageNotification(StorageItem storageItem) {
        super(storageItem.getStorageBox().getStorage().getMember());
        this.storageItem = storageItem;
    }
}
