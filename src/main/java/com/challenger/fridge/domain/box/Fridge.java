package com.challenger.fridge.domain.box;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@DiscriminatorValue("FRIDGE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Fridge extends StorageBox {

    public Fridge(String name) {
        super(name);
    }

    public static Fridge createFridge(String name) {
        return new Fridge(name);
    }
}
