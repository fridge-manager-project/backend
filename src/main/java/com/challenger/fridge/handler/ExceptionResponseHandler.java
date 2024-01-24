package com.challenger.fridge.handler;

import com.challenger.fridge.dto.ApiResponse;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
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

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ApiResponse> handleSignatureException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("토큰이 유효하지 않습니다."));
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ApiResponse> handleMalformedJwtException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("올바르지 않은 토큰입니다."));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse> handleExpiredJwtException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("토큰이 만료되었습니다. 다시 로그인해주세요."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException() {
        return ResponseEntity.internalServerError().body(ApiResponse.error("서버에 문제가 발생했습니다."));
    }

    @ExceptionHandler(StorageItemNotFoundException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse handleStorageItemNotFoundException(StorageItemNotFoundException e) {
        return ApiResponse.error(e.getMessage());
    }

    @ExceptionHandler(StorageNotFoundException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse handleStorageNotFoundException(StorageNotFoundException e) {
        return ApiResponse.error(e.getMessage());
    }

    @ExceptionHandler(ItemNotFoundException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse handleItemNotFoundException(ItemNotFoundException e) {
        return ApiResponse.error(e.getMessage());
    }

    @ExceptionHandler(StorageMethodMachingException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiResponse handleStorageMethodException(StorageMethodMachingException e) {
        return ApiResponse.error(e.getMessage());
    }
}
