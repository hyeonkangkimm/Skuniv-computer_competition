package com.example.skunivProject.domain.recruit.Dto;

import lombok.*;

public class ResponseDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecruitResDto {
        private Long id;
        private String title;
        private String content;
        private String competitionTitle;
        private String writerName;

    }
}
