package com.challenger.fridge.repository;

import com.challenger.fridge.domain.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;

public interface StorageRepository extends JpaRepository<Storage,Long> {

    @Query("select s from Storage s join fetch s.storageItemList sl " +
            "join fetch sl.item i " +
            "join fetch i.category " +
            "where s.id=:storageId and sl.id=:storageItemId")
    Optional<Storage> findStorageItemDetailsById(@Param("storageId") Long storageId, @Param("storageItemId") Long storageItemId);

    @Query("select distinct s from Storage s " +
            "join fetch s.storageItemList sl join fetch sl.item i " +
            "join fetch i.category c where s.id=:storageId")
    Optional<Storage> findStorageItemsById(@Param("storageId") Long storageId);
}
