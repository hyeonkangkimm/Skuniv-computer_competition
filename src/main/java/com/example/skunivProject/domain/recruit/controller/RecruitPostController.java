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

    @PostMapping("/recruit")
    public ResponseEntity<ResponseDto.RecruitResDto> createRecruitPost(
            @RequestBody RequestDto.RecruitReqDto dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        ResponseDto.RecruitResDto response = recruitPostService.createRecruitPost(dto, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/recruit/{postId}/apply")
    public ResponseEntity<ResponseDto.Apply> applyToRecruitPost(
            @PathVariable Long postId,
            @RequestBody RequestDto.Apply dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        ResponseDto.Apply response = recruitPostService.applyToRecruitPost(postId, dto, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/recruit/{postId}/applies")
    public ResponseEntity<List<ResponseDto.ApplyDetail>> getAppliesForPost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        List<ResponseDto.ApplyDetail> response = recruitPostService.getAppliesForPost(postId, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my/recruit-posts")
    public ResponseEntity<List<ResponseDto.MyPost>> getMyRecruitPosts(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        List<ResponseDto.MyPost> response = recruitPostService.getMyRecruitPosts(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/recruit/applies/{applyId}")
    public ResponseEntity<ResponseDto.UpdateApplyStatus> updateApplyStatus(
            @PathVariable Long applyId,
            @RequestBody RequestDto.UpdateApplyStatus dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        ResponseDto.UpdateApplyStatus response = recruitPostService.updateApplyStatus(applyId, dto, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/competitions/{competitionId}/recruits")
    public ResponseEntity<List<ResponseDto.RecruitPostSummaryDto>> getOpenRecruitPostsByCompetition(
            @PathVariable Long competitionId
    ) {
        List<ResponseDto.RecruitPostSummaryDto> response = recruitPostService.getOpenRecruitPosts(competitionId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/recruit/{postId}")
    public ResponseEntity<Void> deleteRecruitPost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        recruitPostService.deleteRecruitPost(postId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/competitions/{competitionId}/random-apply")
    public ResponseEntity<ResponseDto.RandomApplyResult> randomApply(
            @PathVariable Long competitionId,
            @RequestBody RequestDto.RandomApply dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        ResponseDto.RandomApplyResult response = recruitPostService.randomApply(competitionId, dto, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
