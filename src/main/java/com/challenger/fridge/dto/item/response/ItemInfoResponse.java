package com.challenger.fridge.dto.item.response;

import com.challenger.fridge.domain.Item;
import lombok.Getter;

@Getter
public class ItemInfoResponse {

    private Long itemId;
    private String itemName;
    private String categoryName;

    public ItemInfoResponse(Item item) {
        this.itemId = item.getId();
        this.itemName = item.getItemName();
        this.categoryName = item.getCategory().getCategoryName();
    }
}
