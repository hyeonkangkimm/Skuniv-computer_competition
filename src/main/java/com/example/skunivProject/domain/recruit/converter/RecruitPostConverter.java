package com.example.skunivProject.domain.recruit.converter;

import com.example.skunivProject.domain.competition.entity.Competition;
import com.example.skunivProject.domain.recruit.Dto.RequestDto;
import com.example.skunivProject.domain.recruit.Dto.ResponseDto;
import com.example.skunivProject.domain.recruit.entity.RecruitPost;
import com.example.skunivProject.domain.recruit.enums.PostStatus;
import com.example.skunivProject.domain.users.entity.Users;
import org.springframework.stereotype.Component;

@Component
public class RecruitPostConverter {

    // Request DTO → Entity
    public RecruitPost toEntity(RequestDto.RecruitReqDto dto, Competition competition, Users writer) {
        return RecruitPost.builder()
                .competition(competition)
                .writer(writer)
                .title(dto.getTitle())
                .content(dto.getContent())
                .status(PostStatus.OPEN) // 기본 상태
                .maxCapacity(dto.getMaxCapacity())
                .build();
    }

    // Entity → Response DTO
    public ResponseDto.RecruitResDto toResponseDto(RecruitPost post) {
        return ResponseDto.RecruitResDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .competitionTitle(post.getCompetition().getTitle())
                .writerName(post.getWriter().getName())
                .maxCapacity(post.getMaxCapacity())
                .build();
    }

    // Entity → MyPost DTO
    public ResponseDto.MyPost toMyPostDto(RecruitPost post) {
        return ResponseDto.MyPost.builder()
                .postId(post.getId())
                .postTitle(post.getTitle())
                .status(post.getStatus())
                .createdAt(post.getCreatedAt())
                .maxCapacity(post.getMaxCapacity())
                .build();
    }
}
