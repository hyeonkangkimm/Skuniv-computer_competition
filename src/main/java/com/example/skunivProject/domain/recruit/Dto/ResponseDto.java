package com.example.skunivProject.domain.recruit.Dto;

import com.example.skunivProject.domain.recruit.enums.ApplyStatus;
import com.example.skunivProject.domain.recruit.enums.PostStatus;
import com.example.skunivProject.domain.users.enums.Rank;
import lombok.*;

import java.time.LocalDateTime;

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
        private int maxCapacity;
    }

    @Getter
    @Builder
    public static class Apply {
        private Long applyId;
        private Long recruitPostId;
        private String applicantName;
        private ApplyStatus status;
        private String applicantPhone;
    }

    @Getter
    @Builder
    public static class ApplyDetail {
        private Long applyId;
        private Long applicantId;
        private String applicantName;
        private Rank applicantRank;
        private String content;
        private ApplyStatus status;
        private String applicantPhone;
    }

    @Getter
    @Builder
    public static class MyPost {
        private Long postId;
        private String postTitle;
        private PostStatus status;
        private LocalDateTime createdAt;
        private int maxCapacity;
        private String content;
    }

    @Getter
    @Builder
    public static class UpdateApplyStatus {
        private Long applyId;
        private ApplyStatus status;
    }


    @Getter
    @Builder
    public static class RecruitPostSummaryDto {
        private Long postId;
        private String title;
        private String content;
        private String writerName;
        private PostStatus status;
        private long currentAcceptedCount;
        private int maxCapacity;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    public static class TeamCreation {
        private Long teamId;
        private int memberCount;
    }

    /**
     * 1인 참가(랜덤 신청) 성공 시 결과를 담는 DTO
     */
    @Getter
    @Builder
    public static class RandomApplyResult {
        private Long appliedPostId;
        private String postTitle;
        private String writerName;
    }
}
