package com.kokasin.insole.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class FcmMessage {
    private boolean validate_only;
    private Message message;

    @Builder
    @AllArgsConstructor
    public static class Message {
        private Notification notification; // 모든 mobile os를 아우를수 있는 Notification
        private String token; // 특정 device에 알림을 보내기위해 사용
    }

    @Builder
    @AllArgsConstructor
    public static class Notification {
        private String title;
        private String body;
        private String image;
    }
}
