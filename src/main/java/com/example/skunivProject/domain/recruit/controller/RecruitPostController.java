package com.example.skunivProject.domain.recruit.controller;

import com.example.skunivProject.domain.recruit.Dto.RequestDto;
import com.example.skunivProject.domain.recruit.Dto.ResponseDto;
import com.example.skunivProject.domain.recruit.service.RecruitPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recruit")
@RequiredArgsConstructor
public class RecruitPostController {

    private final RecruitPostService recruitPostService;

    /**
     * 모집공고 작성
     * - JWT 인증 필요
     * - Rank.RECRUIT 체크는 Service에서 처리
     */
    @PostMapping
    public ResponseEntity<ResponseDto.RecruitResDto> createRecruitPost(
            @RequestBody RequestDto.RecruitReqDto dto,
            @AuthenticationPrincipal UserDetails userDetails // Spring Security의 UserDetails로 받음
    ) {
        // 서비스에는 username 문자열을 전달
        ResponseDto.RecruitResDto response = recruitPostService.createRecruitPost(dto, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }
}
