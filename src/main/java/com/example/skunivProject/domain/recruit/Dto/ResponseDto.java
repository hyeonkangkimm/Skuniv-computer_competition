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
    }

    @Getter
    @Builder
    public static class UpdateApplyStatus {
        private Long applyId;
        private ApplyStatus status;
    }

    @Getter
    @Builder
    public static class FinalTeamMember {
        private Long userId;
        private String name;
        private Rank rank;
        private String phone;
    }

    @Getter
    @Builder
    public static class RecruitPostSummaryDto {
        private Long postId;
        private String title;
        private String content;
        private String writerName;
        private PostStatus status;
        private long currentAcceptedCount; // 현재 승인된 인원
        private int maxCapacity;           // 최대 인원
        private LocalDateTime createdAt;
    }
}
