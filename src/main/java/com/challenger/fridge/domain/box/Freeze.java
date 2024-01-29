package com.challenger.fridge.domain.box;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("FREEZE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Freeze extends StorageBox {
    public Freeze(String name) {
        super(name);
    }

    public static Freeze createFridge(String name)
    {
        Freeze freeze=new Freeze(name);
        return freeze;
    }
}
