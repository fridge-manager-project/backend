package com.challenger.fridge.controller;

import com.challenger.fridge.dto.ApiResponse;
import com.challenger.fridge.dto.item.response.ItemInfoResponse;
import com.challenger.fridge.service.ItemService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<ApiResponse> itemCategoryInfo() {
        List<ItemInfoResponse> itemInfoResponses = itemService.itemInfo();
        return ResponseEntity.ok(ApiResponse.success(itemInfoResponses));
    }
}
