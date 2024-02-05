package com.challenger.fridge.repository;

import com.challenger.fridge.domain.StorageItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageItemRepository extends JpaRepository<StorageItem, Long> {
}
