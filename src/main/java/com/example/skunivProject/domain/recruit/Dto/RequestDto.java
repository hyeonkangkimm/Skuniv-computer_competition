package com.example.skunivProject.domain.recruit.Dto;

import com.example.skunivProject.domain.recruit.enums.ApplyStatus;
import lombok.*;

public class RequestDto {
    //모집 글 생성
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RecruitReqDto {
        private Long competitionId;
        private String title;
        private String content;
        private int maxCapacity;
    }

    //지원자의 신청글
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Apply {
        private String content;
    }

    //지원자의 상태 변경
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateApplyStatus {
        private ApplyStatus status;
    }
}
