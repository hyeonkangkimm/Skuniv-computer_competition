package com.example.skunivProject.domain.team.controller;

import com.example.skunivProject.domain.recruit.Dto.ResponseDto;
import com.example.skunivProject.domain.team.dto.ResponseDto.MyFinishedCompetitionDto;
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

    //최종 팀 확정
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

    //내팀조회 누구나 요청 가능 근데 자기팀만 나옴 리스트형식으로
    @GetMapping("/my/teams")
    public ResponseEntity<List<MyTeamDto>> getMyTeams(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        List<MyTeamDto> response = teamService.getMyTeams(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    //팀 종료 - 공모전이 끝나고 모든 팀 삭제
    @PostMapping("/my/teams/{teamId}/finish")
    public ResponseEntity<Void> finishTeam(
            @PathVariable Long teamId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        teamService.finishTeam(teamId, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    //끝낸 공모전 모음 -> 공모전 끝내고 난 다음 내페이지에 내가한 공모전 리스트
    @GetMapping("/my/teams/finished")
    public ResponseEntity<List<MyFinishedCompetitionDto>> getMyFinishedTeams(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        List<MyFinishedCompetitionDto> response = teamService.getMyFinishedTeams(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }
}
