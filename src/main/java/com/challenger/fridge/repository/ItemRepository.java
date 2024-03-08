package com.challenger.fridge.repository;


import com.challenger.fridge.domain.Item;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i from Item i join fetch i.category c")
    List<Item> findAllWithCategory();
}
