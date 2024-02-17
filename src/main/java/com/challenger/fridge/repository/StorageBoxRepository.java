package com.challenger.fridge.repository;

import com.challenger.fridge.domain.box.StorageBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface StorageBoxRepository extends JpaRepository<StorageBox, Long> {
    @Query("select distinct sb from StorageBox sb " +
            "left join fetch sb.storageItemList sl " +
            "left join fetch sl.item i " +
            "left join fetch i.category c " +
            "where sb.id=:storageBoxId " +
            "and c.categoryName in :categories")
    Optional<StorageBox> findStorageItemsByIdAndCategories(@Param("storageBoxId") Long storageBoxId, @Param("categories") List<String> categories);

    @Query("select distinct sb from StorageBox sb " +
            "left join fetch sb.storageItemList sl " +
            "left join fetch sl.item i " +
            "left join fetch i.category c " +
            "where sb.id=:storageBoxId ")
    Optional<StorageBox> findStorageItemsById(@Param("storageBoxId") Long storageBoxId);
}
