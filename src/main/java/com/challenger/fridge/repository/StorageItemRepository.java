package com.challenger.fridge.repository;

import com.challenger.fridge.domain.StorageItem;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface StorageItemRepository extends JpaRepository<StorageItem,Long> {
    @Query("select si from StorageItem si join fetch si.storage s " +
            "join fetch si.item i " +
            "join fetch i.category where si.id=:storageItemId ")
    Optional<StorageItem> findStorageItemDetailsById(@Param("storageItemId") Long storageItemId);

}
