package com.challenger.fridge.repository;

import com.challenger.fridge.domain.box.StorageBox;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StorageBoxRepository extends JpaRepository<StorageBox, Long> {

}
