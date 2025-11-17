package com.example.skunivProject.domain.recruit.Dto;

import lombok.*;

public class RequestDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecruitReqDto {
        private Long competitionId;
        private String title;
        private String content;

    }
}
