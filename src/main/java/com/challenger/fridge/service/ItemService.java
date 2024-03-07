package com.challenger.fridge.service;

import com.challenger.fridge.domain.Item;
import com.challenger.fridge.dto.item.response.ItemInfoResponse;
import com.challenger.fridge.repository.ItemRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    @Cacheable(key = "'all'")
    public List<ItemInfoResponse> itemInfo() {
        log.info("상품 전체 데이터 조회");
        List<Item> items = itemRepository.findAllWithCategory();
        return items.stream()
                .map(ItemInfoResponse::new)
                .collect(Collectors.toList());
    }
}
