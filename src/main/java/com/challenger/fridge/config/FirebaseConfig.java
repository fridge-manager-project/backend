package com.challenger.fridge.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Slf4j
@Configuration
public class FirebaseConfig {

    @Value("${firebase.sdk.path}")
    private String firebaseSdkPath;

    @Bean
    public FirebaseApp firebaseApp() {
        if (FirebaseApp.getApps().isEmpty()) {
            try {
                ClassPathResource resource = new ClassPathResource(firebaseSdkPath);
                InputStream inputStream = resource.getInputStream();
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(inputStream))
                        .build();
                return FirebaseApp.initializeApp(options);
            } catch (FileNotFoundException e) {
                log.error("Firebase ServiceAccountKey FileNotFoundException : " + e.getMessage());
                throw new IllegalArgumentException("FileNotFoundException : Firebase ServiceAccountKey 을 찾을 수 없습니다.");
            } catch (IOException e) {
                log.error("FirebaseOptions IOException : " + e.getMessage());
                throw new IllegalArgumentException("IOException : Firebase IOException");
            }
        } else {
            return FirebaseApp.getInstance();
        }
    }

    @Bean
    public FirebaseMessaging firebaseMessaging() {
        return FirebaseMessaging.getInstance(firebaseApp());
    }
}
