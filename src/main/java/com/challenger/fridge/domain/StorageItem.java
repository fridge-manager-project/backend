package com.challenger.fridge.domain;

import static jakarta.persistence.FetchType.*;

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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "storage_id")
    private Storage storage;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private Long quantity;

    private LocalDateTime expirationDate;

    private LocalDateTime purchaseDate;

    public StorageItem(Storage storage, Item item, Long quantity, LocalDateTime expirationDate, LocalDateTime purchaseDate) {
        this.storage = storage;
        this.item = item;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
        this.purchaseDate = purchaseDate;
    }
}
