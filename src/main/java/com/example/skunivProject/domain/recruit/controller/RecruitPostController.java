package com.example.skunivProject.domain.recruit.controller;

import com.example.skunivProject.domain.recruit.Dto.RequestDto;
import com.example.skunivProject.domain.recruit.Dto.ResponseDto;
import com.example.skunivProject.domain.recruit.service.RecruitPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RecruitPostController {

    private final RecruitPostService recruitPostService;

    //모집공고 올리기
    @PostMapping("/recruit")
    public ResponseEntity<ResponseDto.RecruitResDto> createRecruitPost(
            @RequestBody RequestDto.RecruitReqDto dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        ResponseDto.RecruitResDto response = recruitPostService.createRecruitPost(dto, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //모집공고 지원
    @PostMapping("/recruit/{postId}/apply")
    public ResponseEntity<ResponseDto.Apply> applyToRecruitPost(
            @PathVariable Long postId,
            @RequestBody RequestDto.Apply dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        ResponseDto.Apply response = recruitPostService.applyToRecruitPost(postId, dto, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //공고 지원자 목록 리스트 조회
    @GetMapping("/recruit/{postId}/applies")
    public ResponseEntity<List<ResponseDto.ApplyDetail>> getAppliesForPost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        List<ResponseDto.ApplyDetail> response = recruitPostService.getAppliesForPost(postId, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }


    //나의 공고 조회(나무등급) 리스트조회
    @GetMapping("/my/recruit-posts")
    public ResponseEntity<List<ResponseDto.MyPost>> getMyRecruitPosts(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        List<ResponseDto.MyPost> response = recruitPostService.getMyRecruitPosts(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }


    //지원한 사람 상태 변경
    @PatchMapping("/recruit/applies/{applyId}")
    public ResponseEntity<ResponseDto.UpdateApplyStatus> updateApplyStatus(
            @PathVariable Long applyId,
            @RequestBody RequestDto.UpdateApplyStatus dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        ResponseDto.UpdateApplyStatus response = recruitPostService.updateApplyStatus(applyId, dto, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    //특정 공모전의 모집공고 리스트 조회 모든사람 접근 가능 홈화면에서 모집공고 상세 넘어올때
    @GetMapping("/competitions/{competitionId}/recruits")
    public ResponseEntity<List<ResponseDto.RecruitPostSummaryDto>> getOpenRecruitPostsByCompetition(
            @PathVariable Long competitionId
    ) {
        List<ResponseDto.RecruitPostSummaryDto> response = recruitPostService.getOpenRecruitPosts(competitionId);
        return ResponseEntity.ok(response);
    }

    //모집공고 삭제
    @DeleteMapping("/recruit/{postId}")
    public ResponseEntity<Void> deleteRecruitPost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        recruitPostService.deleteRecruitPost(postId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    //1인참가인 사람의 랜덤 참가
    @PostMapping("/recruits/random-apply")
    public ResponseEntity<ResponseDto.RandomApplyResult> randomApply(
            @RequestBody RequestDto.RandomApply dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        ResponseDto.RandomApplyResult response = recruitPostService.randomApply(dto, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
