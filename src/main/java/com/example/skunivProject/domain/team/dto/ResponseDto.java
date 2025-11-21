package com.example.skunivProject.domain.team.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class ResponseDto {

    /**
     * 나의 팀 목록 조회를 위한 응답 DTO
     */
    @Getter
    @Builder
    public static class MyTeamDto {
        private Long userId; // 요청한 사용자의 ID
        private Long teamId; // 팀 ID
        private String competitionTitle; // 공모전 제목
        private List<String> memberNames; // 모든 팀 멤버의 이름 목록
        private String roomStatus; // 채팅방 상태 ("roomId" 또는 "채팅방이 개설되어있지 않습니다.")
    }
}
