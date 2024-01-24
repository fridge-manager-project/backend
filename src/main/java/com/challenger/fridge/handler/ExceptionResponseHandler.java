package com.challenger.fridge.handler;

import com.challenger.fridge.dto.ApiResponse;
import com.challenger.fridge.exception.ItemNotFoundException;
import com.challenger.fridge.exception.StorageItemNotFoundException;
import com.challenger.fridge.exception.StorageMethodMachingException;
import com.challenger.fridge.exception.StorageNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionResponseHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgumentException(Exception e) {
        return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
    }

    @ExceptionHandler(StorageItemNotFoundException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse handleStorageItemNotFoundException(StorageItemNotFoundException e)
    {
        return ApiResponse.error(e.getMessage());
    }

    @ExceptionHandler(StorageNotFoundException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse handleStorageNotFoundException(StorageNotFoundException e)
    {
        return ApiResponse.error(e.getMessage());
    }
    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse handleItemNotFoundException(ItemNotFoundException e)
    {
        return ApiResponse.error(e.getMessage());
    }
    @ExceptionHandler(StorageMethodMachingException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiResponse handleStorageMethodException(StorageMethodMachingException e)
    {
        return ApiResponse.error(e.getMessage());
    }
}
