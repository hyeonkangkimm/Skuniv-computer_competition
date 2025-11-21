package com.example.skunivProject.domain.chat.dto;

import lombok.Builder;
import lombok.Getter;

public class ResponseDto {

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
