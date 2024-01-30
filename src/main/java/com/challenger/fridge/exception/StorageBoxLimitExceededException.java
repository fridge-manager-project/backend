package com.challenger.fridge.exception;

public class StorageBoxLimitExceededException extends RuntimeException{
    public StorageBoxLimitExceededException(String message) {
        super(message);
    }
}
