package com.challenger.fridge.service;

import com.challenger.fridge.domain.Item;
import com.challenger.fridge.dto.item.response.ItemInfoResponse;
import com.challenger.fridge.repository.ItemRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    public List<ItemInfoResponse> itemInfo() {
        List<Item> items = itemRepository.findAllWithCategory();
        return items.stream()
                .map(ItemInfoResponse::new)
                .collect(Collectors.toList());
    }
}
