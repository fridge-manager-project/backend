package com.challenger.fridge.domain;

import static jakarta.persistence.FetchType.*;

import com.challenger.fridge.domain.box.StorageBox;
import com.challenger.fridge.dto.item.request.StorageItemRequest;
import com.challenger.fridge.dto.item.request.StorageItemUpdateRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

import lombok.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StorageItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "storage_item_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "storage_box_id")
    private StorageBox storageBox;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private Long quantity;

    private String itemDescription;

    private LocalDate expirationDate;

    private LocalDate purchaseDate;

    public void addStorageBox(StorageBox storageBox) {
        this.storageBox = storageBox;
        storageBox.getStorageItemList().add(this);
    }

    public void addItem(Item item) {
        this.item = item;
    }

    public StorageItem(Long quantity, String itemDescription, LocalDate expirationDate, LocalDate purchaseDate) {
        this.quantity = quantity;
        this.itemDescription = itemDescription;
        this.expirationDate = expirationDate;
        this.purchaseDate = purchaseDate;
    }

    public StorageItem(Long quantity, Item item, StorageBox storageBox) {
        this.quantity = quantity;
        this.storageBox = storageBox;
        this.item = item;
        this.purchaseDate = LocalDate.now();
    }

    public static StorageItem createStorageItem(StorageItemRequest storageItemRequest, Item item, StorageBox storageBox) {
        StorageItem storageItem = new StorageItem(storageItemRequest.getItemCount()
                , storageItemRequest.getItemDescription()
                , storageItemRequest.getExpireDateAsLocalDate()
                , storageItemRequest.getPurchaseDateAsLocalDate());
        storageItem.addItem(item);
        storageItem.addStorageBox(storageBox);
        return storageItem;
    }

    /**
     * storageItem 필드 변경 로직
     *
     * @param storageItemUpdateRequest
     */
    //PATCH 메서드로 받아오기 떄문에 어떠한 자원이 넘어오는지는 서비스 로직에서는 알수가 없다 그래서 하나씩 필드마다 NULL체크를 해줘야하는 단점이 있다
    //개선해야할 방안을 찾아봐야한다.
    public void changeStorageItem(StorageItemUpdateRequest storageItemUpdateRequest) {

        if (storageItemUpdateRequest.getItemCount() != null) {
            this.quantity = storageItemUpdateRequest.getItemCount();
        }
        if (storageItemUpdateRequest.getItemDescription() != null) {
            this.itemDescription = storageItemUpdateRequest.getItemDescription();
        }
        if (storageItemUpdateRequest.getExpirationDate() != null) {
            this.expirationDate = storageItemUpdateRequest.getExpireDateAsLocalDate();
        }
        if (storageItemUpdateRequest.getPurchaseDate() != null) {
            this.purchaseDate = storageItemUpdateRequest.getPurchaseDateAsLocalDate();
        }
    }
    /**
     * storageItem 다른 StorageBox로 이동
     *
     * @param storageBox
     */
    public void moveStorageItem(StorageBox storageBox)
    {
        this.storageBox=storageBox;
    }


}
