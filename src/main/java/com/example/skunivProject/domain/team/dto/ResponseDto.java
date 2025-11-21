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
        private Long userId;
        private Long teamId;
        private String competitionTitle;
        private List<String> memberNames;
        private String roomStatus;
    }

    /**
     * 내가 참여했던 (종료된) 공모전 목록 조회를 위한 응답 DTO
     */
    @Getter
    @Builder
    public static class MyFinishedCompetitionDto {
        private String competitionTitle;
        private String imgUrl;
        private List<String> memberNames;
    }
}
