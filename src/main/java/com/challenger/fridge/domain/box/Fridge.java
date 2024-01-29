package com.challenger.fridge.domain.box;

import com.challenger.fridge.domain.Storage;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("FRIDGE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Fridge extends StorageBox{

    public Fridge(String name) {
        super(name);
    }

    public static Fridge createFridge(String name)
    {
        Fridge fridge=new Fridge(name);
        return fridge;
    }
}
