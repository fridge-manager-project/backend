package com.challenger.fridge.domain;

import static jakarta.persistence.FetchType.*;

import com.challenger.fridge.domain.box.StorageBox;
import com.challenger.fridge.dto.storage.request.StorageItemRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

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

    private LocalDateTime expirationDate;

    private LocalDateTime purchaseDate;



}
