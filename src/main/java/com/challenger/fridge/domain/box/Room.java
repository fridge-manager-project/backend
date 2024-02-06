package com.challenger.fridge.domain.box;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("ROOM")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Room extends StorageBox{
    public Room(String name) {
        super(name);
    }

    public static Room createFridge(String name)
    {
        Room room=new Room(name);
        return room;
    }
}
