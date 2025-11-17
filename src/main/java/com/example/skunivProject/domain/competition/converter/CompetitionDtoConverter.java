package com.example.skunivProject.domain.competition.converter;

import com.example.skunivProject.domain.competition.dto.ResponseDto;
import com.example.skunivProject.domain.competition.entity.Competition;

import java.util.List;
import java.util.stream.Collectors;

public class CompetitionDtoConverter {

    // 단일 Competition -> HomeResponseDto 변환 (전체 정보 포함)
    public static ResponseDto.HomeResponseDto toHomeResponseDto(Competition competition) {
        return ResponseDto.HomeResponseDto.builder()
                .title(competition.getTitle())
                .host(competition.getHost())
                .organizer(competition.getOrganizer())
                .category(competition.getCategory())
                .qualification(competition.getQualification())
                .applyStart(competition.getApplyStart())
                .applyEnd(competition.getApplyEnd())
                .awardFirst(competition.getAwardFirst())
                .homePage(competition.getHomePage())
                .thinkGoodLink(competition.getThinkGoodLink())
                .imgUrl(competition.getImgUrl())
                .id(competition.getId())
                .build();
    }

    // Competition 리스트 -> HomeResponseDto 리스트 변환
    public static List<ResponseDto.HomeResponseDto> toHomeResponseDtoList(List<Competition> competitions) {
        return competitions.stream()
                .map(CompetitionDtoConverter::toHomeResponseDto)
                .collect(Collectors.toList());
    }
}
