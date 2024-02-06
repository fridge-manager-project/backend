package com.challenger.fridge.exception;

public class StorageNameDuplicateException extends RuntimeException {
    public StorageNameDuplicateException(String message) {
        super(message);
    }
}
