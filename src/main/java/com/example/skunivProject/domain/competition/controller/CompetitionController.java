package com.example.skunivProject.domain.competition.controller;

import com.example.skunivProject.domain.competition.converter.CompetitionDtoConverter;
import com.example.skunivProject.domain.competition.dto.ResponseDto;
import com.example.skunivProject.domain.competition.entity.Competition;
import com.example.skunivProject.domain.competition.repository.CompetitionRepository;
import com.example.skunivProject.global.apipayload.ApiResponse;
import com.example.skunivProject.global.apipayload.code.GeneralSuccessCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CompetitionController {

    private final CompetitionRepository competitionRepository;

    public CompetitionController(CompetitionRepository competitionRepository) {
        this.competitionRepository = competitionRepository;
    }

    //전체 데이터 반환
    @GetMapping("/home")
    public ResponseEntity<ApiResponse<List<ResponseDto.HomeResponseDto>>> getHomeCompetitions() {
        // isDeleted=false인 데이터만 조회
        List<Competition> competitions = competitionRepository.findAllByIsDeletedFalse();

        // DTO 변환
        List<ResponseDto.HomeResponseDto> dtoList = CompetitionDtoConverter.toHomeResponseDtoList(competitions);

        // ApiResponse로 감싸서 반환
        GeneralSuccessCode code = GeneralSuccessCode.SUCCESS;
        return ResponseEntity.status(code.getStatus())
                .body(ApiResponse.onSuccess(code, dtoList));
    }
}
