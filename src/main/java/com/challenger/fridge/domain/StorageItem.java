package com.challenger.fridge.domain;

import static jakarta.persistence.FetchType.*;

import com.challenger.fridge.dto.storage.request.StorageItemRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.parameters.P;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StorageItem {

    @Id @GeneratedValue
    @Column(name = "storage_item_id")
    private Long id;


    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "storage_id")
    private Storage storage;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private Long quantity;

    private LocalDateTime expirationDate;

    private LocalDateTime purchaseDate;

    public void addStorageItem(Storage storage)
    {
        this.storage=storage;
        this.storage.getStorageItemList().add(this);
    }
    private StorageItem(Item item, Long quantity, LocalDateTime expirationDate, LocalDateTime purchaseDate) {
        this.item = item;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
        this.purchaseDate = purchaseDate;
    }

    public static StorageItem createStorageItem(StorageItemRequest storageItemRequest,Item item)
    {
        StorageItem storageItem=new StorageItem(item
                                                ,storageItemRequest.getItemCount()
                                                ,storageItemRequest.getExpireDateAsLocalDateTime()
                                                ,storageItemRequest.getPurchaseDateAsLocalDateTime());

        return storageItem;

    }






}
