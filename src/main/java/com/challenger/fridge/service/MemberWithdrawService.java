package com.challenger.fridge.service;

import com.challenger.fridge.domain.Cart;
import com.challenger.fridge.domain.CartItem;
import com.challenger.fridge.domain.Member;
import com.challenger.fridge.domain.Storage;
import com.challenger.fridge.domain.StorageItem;
import com.challenger.fridge.domain.box.StorageBox;
import com.challenger.fridge.domain.notification.Notification;
import com.challenger.fridge.repository.CartItemRepository;
import com.challenger.fridge.repository.MemberRepository;
import com.challenger.fridge.repository.NotificationRepository;
import com.challenger.fridge.repository.StorageBoxRepository;
import com.challenger.fridge.repository.StorageItemRepository;
import com.challenger.fridge.repository.StorageRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class MemberWithdrawService {

    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    private final StorageRepository storageRepository;
    private final StorageBoxRepository storageBoxRepository;
    private final StorageItemRepository storageItemRepository;
    private final CartItemRepository cartItemRepository;

    public void withdrawMember(String email) {
        Member member = findMemberWithCart(email);
        Cart cart = member.getCart();

        if (!cart.getCartItemList().isEmpty()) {
            List<CartItem> cartItemList = findAllItemsInCart(cart);
            deleteAllItemsInCart(cartItemList);
        }

        List<Notification> notificationList = findAllNotifications(member);
        if(!notificationList.isEmpty()) deleteAllNotification(notificationList);

        List<Storage> storageList = findAllStorage(member);
        List<StorageBox> storageBoxList = findAllStorageBoxInStorage(storageList);
        List<StorageItem> storageItemList = findAllItemsInStorageBox(storageBoxList);

        if (!storageItemList.isEmpty()) {
            deleteAllItemsInStorageBox(storageItemList);
        }
        if (!storageBoxList.isEmpty()) {
            deleteAllStorageBoxInStorage(storageBoxList);
        }
        if (!storageList.isEmpty()) {
            deleteAllStorage(storageList);
        }

        memberRepository.delete(member);
    }

    private List<CartItem> findAllItemsInCart(Cart cart) {
        return cartItemRepository.findCartItemsByCartEquals(cart);
    }

    private void deleteAllItemsInCart(List<CartItem> cartItemList) {
        cartItemRepository.deleteAllInBatch(cartItemList);
    }

    private List<Notification> findAllNotifications(Member member) {
        return notificationRepository.findAllByMemberEquals(member);
    }

    private void deleteAllNotification(List<Notification> notificationList) {
        notificationRepository.deleteAllInListIn(notificationList);
    }

    private void deleteAllStorage(List<Storage> storageList) {
        storageRepository.deleteAllInList(storageList);
    }

    private void deleteAllStorageBoxInStorage(List<StorageBox> storageBoxList) {
        storageBoxRepository.deleteAllListIn(storageBoxList);
    }

    private void deleteAllItemsInStorageBox(List<StorageItem> storageItemList) {
        storageItemRepository.deleteAllInList(storageItemList);
    }

    private List<StorageItem> findAllItemsInStorageBox(List<StorageBox> storageBoxList) {
        return storageItemRepository.findStorageItemsByStorageBoxIn(storageBoxList);
    }

    private List<StorageBox> findAllStorageBoxInStorage(List<Storage> storageList) {
        return storageBoxRepository.findStorageBoxesByStorageListIn(storageList);
    }

    private List<Storage> findAllStorage(Member member) {
        return storageRepository.findStorageListByMember(member);
    }

    private Member findMemberWithCart(String email) {
        return memberRepository.findMemberAndCartByEmail(email).orElseThrow(IllegalArgumentException::new);
    }
}
