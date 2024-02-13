package com.challenger.fridge.controller;

import com.challenger.fridge.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<ApiResponse> home() {
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
