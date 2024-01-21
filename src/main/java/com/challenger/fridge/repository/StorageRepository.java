package com.challenger.fridge.repository;

import com.challenger.fridge.domain.Storage;
import com.challenger.fridge.domain.StorageItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StorageRepository extends JpaRepository<Storage,Long> {


    @Query("select distinct s from Storage s " +
            "join fetch s.storageItemList sl join fetch sl.item i " +
            "join fetch i.category c where s.id=:storageId")
    List<Storage> findByStorageItemList(@Param("storageId") Long storageId);
}
