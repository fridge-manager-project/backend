package com.challenger.fridge.exception;

public class StorageItemNotFoundException extends RuntimeException {
    public StorageItemNotFoundException(String message) {
        super(message);
    }
}
