package com.challenger.fridge.service;

import com.challenger.fridge.domain.Member;
import com.challenger.fridge.domain.Notice;
import com.challenger.fridge.dto.notice.FcmMessage;
import com.challenger.fridge.repository.NoticeRepository;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class FCMService {
    private final NoticeRepository noticeRepository;
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/" +
            "naenggeul-d0686/messages:send";
    private final ObjectMapper objectMapper;

    public void sendTestMessage(List<Member> memberList) throws IOException {
        for(Member member : memberList) {
            if(member.getPushToken() != null) {
                String message = makeMessage(member.getPushToken(), "테스트 알림", "테스트알림입니다.");

                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = RequestBody.create(message,
                        MediaType.get("application/json; charset=utf-8"));
                Request request = new Request.Builder()
                        .url(API_URL)
                        .post(requestBody)
                        .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                        .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                        .build();

                Response response = client.newCall(request).execute();

                noticeRepository.save(Notice.createNotice(member,"유통기한이 임박한 상품이 있습니다."));
                log.info(Objects.requireNonNull(response.body()).string());
            }
            else {
                log.info("테스트용 : 알림은 허용이지만, 토큰이 없는 유저");
            }
        }
    }

    public void sendExpirationMessageTo(List<Member> memberList) throws IOException {
        for(Member member : memberList) {
            if(member.getPushToken() != null) {
                String message = makeMessage(member.getPushToken(), "유통기한 알림", "유통기한이 임박한 상품이 있습니다.");

                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = RequestBody.create(message,
                        MediaType.get("application/json; charset=utf-8"));
                Request request = new Request.Builder()
                        .url(API_URL)
                        .post(requestBody)
                        .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                        .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                        .build();

                Response response = client.newCall(request).execute();

                noticeRepository.save(Notice.createNotice(member,"유통기한이 임박한 상품이 있습니다."));
                log.info(Objects.requireNonNull(response.body()).string());
            }
            else {
                log.info("알림은 허용이지만, 토큰이 없는 유저");
            }
        }
    }

    public void sendCartMessageTo(List<Member> memberList) throws IOException {
        for(Member member : memberList) {
            if(member.getPushToken() != null) {
                String message = makeMessage(member.getPushToken(), "장바구니 알림", "장바구니에 상품이 담겨져 있습니다.");

                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = RequestBody.create(message,
                        MediaType.get("application/json; charset=utf-8"));
                Request request = new Request.Builder()
                        .url(API_URL)
                        .post(requestBody)
                        .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                        .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                        .build();

                Response response = client.newCall(request).execute();

                noticeRepository.save(Notice.createNotice(member, "장바구니에 상품이 담겨져 있습니다."));
                log.info(Objects.requireNonNull(response.body()).string());
            }
            else {
                log.info("알림은 허용이지만, 토큰이 없는 유저");
            }
        }
    }

    private String makeMessage(String targetToken, String title, String body)
            throws JsonParseException, JsonProcessingException {
        FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                        .token(targetToken)
                        .notification(FcmMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();

        return objectMapper.writeValueAsString(fcmMessage);
    }

    public String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/firebase_service_key.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
