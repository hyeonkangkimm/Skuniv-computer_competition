package com.example.skunivProject.domain.recruit.Dto;

import com.example.skunivProject.domain.recruit.enums.ApplyStatus;
import com.example.skunivProject.domain.recruit.enums.RandomApplySetting;
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
        private RandomApplySetting randomApplySetting;
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

    /**
     * 1인 참가(랜덤 신청)를 위한 DTO
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RandomApply {
        private String content;
    }
}
