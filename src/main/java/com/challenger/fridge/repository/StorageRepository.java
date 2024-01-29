package com.challenger.fridge.repository;

import com.challenger.fridge.domain.Storage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageRepository extends JpaRepository<Storage,Long> {
    boolean existsByName(String storageName);
}
