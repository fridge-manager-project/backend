package com.challenger.fridge.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.challenger.fridge.domain.Cart;
import com.challenger.fridge.domain.CartItem;
import com.challenger.fridge.domain.Item;
import com.challenger.fridge.domain.Member;
import com.challenger.fridge.domain.Storage;
import com.challenger.fridge.domain.StorageItem;
import com.challenger.fridge.domain.box.StorageBox;
import com.challenger.fridge.domain.notification.Notice;
import com.challenger.fridge.domain.notification.Notification;
import com.challenger.fridge.domain.notification.StorageNotification;
import com.challenger.fridge.dto.box.request.StorageBoxSaveRequest;
import com.challenger.fridge.dto.box.request.StorageMethod;
import com.challenger.fridge.dto.item.request.StorageItemRequest;
import com.challenger.fridge.dto.member.MemberInfoResponse;
import com.challenger.fridge.dto.box.response.StorageBoxNameResponse;
import com.challenger.fridge.dto.member.ChangePasswordRequest;
import com.challenger.fridge.dto.member.MemberNicknameRequest;
import com.challenger.fridge.dto.sign.SignUpRequest;
import com.challenger.fridge.dto.storage.request.StorageSaveRequest;
import com.challenger.fridge.repository.CartItemRepository;
import com.challenger.fridge.repository.MemberRepository;
import com.challenger.fridge.repository.NotificationRepository;
import com.challenger.fridge.repository.StorageBoxRepository;
import com.challenger.fridge.repository.StorageItemRepository;
import com.challenger.fridge.repository.StorageRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    SignService signService;
    @Autowired
    StorageService storageService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    StorageRepository storageRepository;
    @Autowired
    StorageBoxService storageBoxService;
    @Autowired
    EntityManager em;
    @Autowired
    CartService cartService;
    @Autowired
    StorageBoxRepository storageBoxRepository;
    @Autowired
    StorageItemRepository storageItemRepository;
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    NotificationRepository notificationRepository;

    private static final String EMAIL = "springTest@test.com";
    private static final String PASSWORD = "1234";
    private static final String NAME = "springTest";

    private static final String EMAIL_WITHOUT_BOX = "noStorage@test.com";
    private static final String NAME_WITHOUT_BOX = "noStorage";

    private static final Long fridgeCount = 2L;
    private static final Long freezerCount = 3L;

    private Long mainStorageId;
    private Long subStorageId;
    private Long subStorageId2;

    @BeforeEach
    void setUp() {
        signService.registerMember(new SignUpRequest(EMAIL_WITHOUT_BOX, PASSWORD, NAME_WITHOUT_BOX));
        signService.registerMember(new SignUpRequest(EMAIL, PASSWORD, NAME));
        mainStorageId = storageService.saveStorage(new StorageSaveRequest("메인저장소", fridgeCount, freezerCount), EMAIL);
        storageService.saveStorage(new StorageSaveRequest("메인저장소", 0L, 0L), EMAIL_WITHOUT_BOX);
//        subStorageId = storageService.saveStorage(new StorageSaveRequest("서브저장소", 1L, 1L), EMAIL);
//        subStorageId2 = storageService.saveStorage(new StorageSaveRequest("두번째서브저장소", 1L, 1L), EMAIL);
        Long testStorageBoxId = addTestStorageBox();
        addTestStorageItem(testStorageBoxId);
        addNotifications();
        cartService.addItem(EMAIL, 11L);
        cartService.addItem(EMAIL, 12L);
        cartService.addItem(EMAIL, 13L);
    }

    private void addNotifications() {
        List<StorageItem> storageItemList = em.createQuery(
                        "select si from StorageItem si where si.storageBox.storage.member.email = :email", StorageItem.class)
                .setParameter("email", EMAIL)
                .getResultList();
        em.persist(new Notice(findMember(), "테스트공지", "테스트요"));
        storageItemList.forEach(storageItem -> em.persist(new StorageNotification(storageItem)));
//        em.persist(new StorageNotification(storageItemList.get(0)));
    }

    private Member findMember() {
        return memberRepository.findByEmail(EMAIL).orElseThrow(IllegalArgumentException::new);
    }

    private Long addTestStorageBox() {
        return storageService.saveStorageBox(new StorageBoxSaveRequest("테스트냉장실", StorageMethod.FRIDGE),
                mainStorageId);
    }

    private void addTestStorageItem(Long testStorageBoxId) {
        List<Item> itemList = em.createQuery("select i from Item i where i.id between :firstId and :lastId",
                        Item.class)
                .setParameter("firstId", 1)
                .setParameter("lastId", 5)
                .getResultList();
        itemList.stream()
                .map(item -> new StorageItemRequest(item.getId(), item.getItemName(), 2L, "테스트상품", "2024-04-20",
                        "2024-04-20"))
                .forEach(storageItemRequest -> storageBoxService.saveStorageItem(storageItemRequest, testStorageBoxId));
    }

    @Test
    void test() {

    }

    @DisplayName("메인 보관소가 있는 회원 정보 조회")
    @Test
    void memberInfo() {
        String email = EMAIL;

        MemberInfoResponse memberInfo = memberService.findUserInfo(email);
        List<StorageBoxNameResponse> storageBoxes = memberInfo.getStorageBoxes();

        assertThat(memberInfo.getUsername()).isEqualTo(NAME);
        assertThat(memberInfo.getEmail()).isEqualTo(EMAIL);
        assertThat(memberInfo.getMainStorageId()).isEqualTo(mainStorageId);
        assertThat(memberInfo.getMainStorageName()).isEqualTo("메인저장소");

        assertThat(storageBoxes.size()).isEqualTo(fridgeCount + freezerCount);
        assertThat(storageBoxes.get(0).getStorageBoxName()).isEqualTo("냉장고1");
        assertThat(storageBoxes.get(1).getStorageBoxName()).isEqualTo("냉장고2");
        assertThat(storageBoxes.get(2).getStorageBoxName()).isEqualTo("냉동고1");
        assertThat(storageBoxes.get(3).getStorageBoxName()).isEqualTo("냉동고2");
        assertThat(storageBoxes.get(4).getStorageBoxName()).isEqualTo("냉동고3");
    }

    @DisplayName("메인 보관소가 없는 회원 정보 조회")
    @Test
    void memberInfoWithoutStorage() {
        String email = EMAIL_WITHOUT_BOX;

        MemberInfoResponse memberInfo = memberService.findUserInfo(email);

        assertThat(memberInfo.getUsername()).isEqualTo(NAME_WITHOUT_BOX);
        assertThat(memberInfo.getEmail()).isEqualTo(EMAIL_WITHOUT_BOX);
        assertThat(memberInfo.getMainStorageId()).isNull();
        assertThat(memberInfo.getMainStorageName()).isNull();
        assertThat(memberInfo.getStorageBoxes()).isNull();
    }

    @DisplayName("현재 비밀번호칸에 틀린 비밀번호 입력시 예외 발생")
    @Test
    void changeMemberInfoWithSamePassword() {
        String email = EMAIL;
        String currentPassword = "4321";
        String newPassword = "newPassword";

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(currentPassword, newPassword);

        assertThrows(IllegalArgumentException.class, () ->
                memberService.changeUserInfo(email, changePasswordRequest));
    }

    @DisplayName("새로운 비밀번호로 회원 정보 수정")
    @Test
    void changeMemberInfoWithNewPassword() {
        String email = EMAIL;
        String currentPassword = PASSWORD;
        String newPassword = "newPassword";
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(currentPassword, newPassword);

        memberService.changeUserInfo(email, changePasswordRequest);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(IllegalArgumentException::new);

        assertThat(encoder.matches(newPassword, member.getPassword())).isTrue();
    }

    @DisplayName("현재 닉네임과 같은 닉네임 입력시 예외 발생")
    @Test
    void changeToSameNickname() {
        String email = EMAIL;
        String newNickname = NAME;
        MemberNicknameRequest memberNicknameRequest = new MemberNicknameRequest(newNickname);

        assertThrows(IllegalArgumentException.class, () ->
                memberService.changeUserNickname(email, memberNicknameRequest));
    }

    @DisplayName("닉네임 변경 테스트")
    @Test
    void changeNickname() {
        String email = EMAIL;
        String newNickname = "newNickname";
        MemberNicknameRequest memberNicknameRequest = new MemberNicknameRequest(newNickname);

        Long memberId = memberService.changeUserNickname(email, memberNicknameRequest);
        Member memberWithNewNickname = memberRepository.findById(memberId)
                .orElseThrow(IllegalArgumentException::new);

        assertThat(memberWithNewNickname.getNickname()).isEqualTo(newNickname);
    }

    @DisplayName("회원 탈퇴 테스트")
    @ParameterizedTest
    @ValueSource(strings = {EMAIL, EMAIL_WITHOUT_BOX})
    void lastOne(String email) {
        Member member = memberRepository.findMemberAndCartByEmail(email).orElseThrow(IllegalArgumentException::new);
        Cart cart = member.getCart();

        System.out.println("============");
        if(!cart.getCartItemList().isEmpty()) {
            List<CartItem> cartItemList = cartItemRepository.findCartItemsByCartEquals(cart);
            cartItemRepository.deleteAllInBatch(cartItemList);
        }

        List<Notification> notificationList = notificationRepository.findAllByMemberEquals(member);
        if(!notificationList.isEmpty()) notificationRepository.deleteAllInListIn(notificationList);

        List<Storage> storageList = storageRepository.findStorageListByMember(member);
        List<StorageBox> storageBoxList = storageBoxRepository.findStorageBoxesByStorageListIn(storageList);
        List<StorageItem> storageItemList = storageItemRepository.findStorageItemsByStorageBoxIn(storageBoxList);

        if(!storageItemList.isEmpty()) storageItemRepository.deleteAllInList(storageItemList);
        if(!storageBoxList.isEmpty()) storageBoxRepository.deleteAllListIn(storageBoxList);
        if(!storageList.isEmpty()) storageRepository.deleteAllInList(storageList);
        memberRepository.delete(member);
        System.out.println("============");

        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        assertThat(optionalMember.isPresent()).isFalse();
        assertThat(memberRepository.existsByEmail(email)).isFalse();
    }

}