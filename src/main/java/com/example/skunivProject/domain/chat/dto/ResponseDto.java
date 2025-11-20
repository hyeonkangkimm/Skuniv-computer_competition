package com.example.skunivProject.domain.chat.dto;

import lombok.*;

public class ResponseDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ChatMessage {   // 웹소켓 → Kafka → 웹소켓 브로드캐스트용
        private String roomId;
        private String type;
        private MessageBody body;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class MessageBody {
            private Long senderId;
            private String senderName;
            private String message;
            private Long timestamp;
        }
    }

    @Getter
    @Builder
    public static class ChatRoomCreation {
        private String roomId;
        private String roomName;
        private Long teamId;
    }

    /**
     * 내가 속한 채팅방 목록 조회를 위한 DTO
     */
    @Getter
    @Builder
    public static class ChatRoomInfo {
        private String roomId;
        private String roomName;
        private int memberCount;
    }
}
