package com.challenger.fridge.repository;

import com.challenger.fridge.domain.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("select ci from CartItem ci join fetch ci.item i"
            + " join fetch  i.category"
            + " where ci.id = :id")
    CartItem findItemsById(@Param("id") Long id);
}