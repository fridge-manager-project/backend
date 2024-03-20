package com.challenger.fridge.domain.notification;

import com.challenger.fridge.domain.StorageItem;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue(value = "storage")
public class StorageNotification extends Notification {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storage_item_id")
    private StorageItem storageItem;
}
