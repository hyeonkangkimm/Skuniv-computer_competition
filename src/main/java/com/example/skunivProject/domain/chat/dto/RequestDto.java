package com.example.skunivProject.domain.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class RequestDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChatMessageDto {
        private String roomId;
        private Long userId;
        private String username;
        private String name;
        private String message;
        private long timestamp;
    }

    @Data
    public static class ChatSocketRequest {
        private String type; // ENTER / MESSAGE
        private String roomId;
        private Long userId;
        private String username;
        private String name;
        private String message;
    }

}
