package com.challenger.fridge.repository;


import com.challenger.fridge.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ItemRepository extends JpaRepository<Item, Long> {

}
