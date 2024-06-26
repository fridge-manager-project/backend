package com.challenger.fridge.repository;

import com.challenger.fridge.domain.Cart;
import com.challenger.fridge.domain.CartItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("select ci from CartItem ci join fetch ci.item i"
            + " join fetch  i.category"
            + " where ci.id = :id")
    CartItem findItemsById(@Param("id") Long id);

    @Query("select ci from CartItem ci join fetch ci.cart c"
            + " join fetch c.member m"
            + " where m.email = :email")
    List<CartItem> findByEmail(@Param("email") String email);

    @Query("select ci from CartItem ci join fetch ci.cart c"
            + " join fetch c.member m"
            + " where m.email = :email and ci.isPurchased = true")
    List<CartItem> findPurchasedItemByEmail(@Param("email") String email);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("delete from CartItem ci where ci in :cartItemList")
    void deleteMovedItemInBatch(@Param("cartItemList") List<CartItem> cartItemList);

    @Modifying(flushAutomatically = true)
    @Query("delete from CartItem ci where ci.cart.member.email in :email")
    void deleteByMemberEmail(@Param("email") String email);

    @Modifying(flushAutomatically = true)
    @Query("delete from CartItem ci where ci in :cartItemList")
    void deleteAllByInList(@Param("cartItemList") List<CartItem> cartItemList);

    List<CartItem> findCartItemsByCartEquals(Cart cart);
}
