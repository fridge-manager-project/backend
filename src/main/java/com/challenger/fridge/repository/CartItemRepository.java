package com.challenger.fridge.repository;

import com.challenger.fridge.domain.CartItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("select ci from CartItem ci join fetch ci.item i"
            + " join fetch  i.category"
            + " where ci.id = :id")
    CartItem findItemsById(@Param("id") Long id);

    @Modifying(clearAutomatically = true)
    @Query("delete from CartItem ci where ci.id in :idList")
    void deleteCartItemByIds(@Param("idList") List<Long> idList);

    @Query("select ci from CartItem ci join fetch ci.cart c"
            + " join fetch c.member m"
            + " where m.email = :email")
    List<CartItem> findByEmail(@Param("email") String email);

    @Query("delete from CartItem ci where ci.cart.member.email = :email")
    void deleteByEmail(@Param("email") String email);
}
