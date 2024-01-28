package com.challenger.fridge.domain;

import static jakarta.persistence.FetchType.*;

import com.challenger.fridge.common.StorageStatus;
import com.challenger.fridge.domain.box.StorageBox;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Storage {

    @Id @GeneratedValue
    @Column(name = "storage_id")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private StorageStatus status;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "storage")
    private List<StorageBox> storageBoxList = new ArrayList<>();


}
