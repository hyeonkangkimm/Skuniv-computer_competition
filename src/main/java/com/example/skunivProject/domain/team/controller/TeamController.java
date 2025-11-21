package com.example.skunivProject.domain.team.controller;

import com.example.skunivProject.domain.recruit.Dto.ResponseDto;
import com.example.skunivProject.domain.team.dto.ResponseDto.MyTeamDto;
import com.example.skunivProject.domain.team.entity.Team;
import com.example.skunivProject.domain.team.service.TeamService;
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
public class TeamController {

    private final TeamService teamService;

    /**
     * 최종 팀 확정
     */
    @PostMapping("/recruits/{postId}/confirm-team")
    public ResponseEntity<ResponseDto.TeamCreation> confirmTeam(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Team newTeam = teamService.createTeamFromRecruit(postId, userDetails.getUsername());

        ResponseDto.TeamCreation response = ResponseDto.TeamCreation.builder()
                .teamId(newTeam.getId())
                .memberCount(newTeam.getMembers().size())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 나의 팀 목록 조회
     */
    @GetMapping("/my/teams")
    public ResponseEntity<List<MyTeamDto>> getMyTeams(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        List<MyTeamDto> response = teamService.getMyTeams(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }
}
