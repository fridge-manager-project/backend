package com.challenger.fridge.repository;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FCMTokenRepository {

    private final RedisTemplate<String, String> tokenRedisTemplate;

    public void saveFCMToken(String email, String deviceToken) {
        ValueOperations<String, String> operations = tokenRedisTemplate.opsForValue();
        operations.set("FCM:" + email, deviceToken, 86400 * 30 * 2, TimeUnit.SECONDS);
    }

    public String getFCMToken(String email) {
        return tokenRedisTemplate.opsForValue().get("FCM:" + email);
    }

    public void deleteFCMToken(String email) {
        tokenRedisTemplate.delete("FCM:" + email);
    }

}
