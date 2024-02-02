package com.challenger.fridge.exception;

public class StorageBoxNotFoundException extends RuntimeException {
    public StorageBoxNotFoundException(String message) {
        super(message);
    }
}
