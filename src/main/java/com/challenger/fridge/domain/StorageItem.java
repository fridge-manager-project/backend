package com.challenger.fridge.domain;

import static jakarta.persistence.FetchType.*;

import com.challenger.fridge.domain.box.StorageBox;
import com.challenger.fridge.dto.item.StorageItemDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
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
    @GeneratedValue
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

    public void addStorageBox(StorageBox storageBox)
    {
        this.storageBox=storageBox;
        storageBox.getStorageItemList().add(this);
    }

    public void addItem(Item item)
    {
        this.item=item;
    }

    public StorageItem(Long quantity, String itemDescription, LocalDate expirationDate, LocalDate purchaseDate) {
        this.quantity = quantity;
        this.itemDescription = itemDescription;
        this.expirationDate = expirationDate;
        this.purchaseDate = purchaseDate;
    }

    public static StorageItem createStorageItem(StorageItemDto storageItemDto,Item item,StorageBox storageBox)
    {
        System.out.println(storageItemDto.getPurchaseDateAsLocalDate());
        StorageItem storageItem=new StorageItem(storageItemDto.getItemCount()
                                            ,storageItemDto.getItemDescription()
                                            ,storageItemDto.getExpireDateAsLocalDate()
                                            ,storageItemDto.getPurchaseDateAsLocalDate());
        storageItem.addItem(item);
        storageItem.addStorageBox(storageBox);
        return storageItem;
    }


}
