package com.example.skunivProject.domain.chat.dto;

import lombok.*;

public class RequestDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChatMessageSend {   // 사용자가 WebSocket으로 메시지 보낼 때
        private String roomId;
        private Long senderId;
        private String senderName;
        private String message;
    }
}
