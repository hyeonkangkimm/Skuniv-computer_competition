package com.example.skunivProject.domain.team.service;

import com.example.skunivProject.domain.chat.repository.ChatMessageRepository;
import com.example.skunivProject.domain.chat.repository.ChatRoomRepository;
import com.example.skunivProject.domain.recruit.entity.RecruitApply;
import com.example.skunivProject.domain.recruit.entity.RecruitPost;
import com.example.skunivProject.domain.recruit.enums.ApplyStatus;
import com.example.skunivProject.domain.recruit.enums.PostStatus;
import com.example.skunivProject.domain.recruit.exception.RecruitException;
import com.example.skunivProject.domain.recruit.exception.code.RecruitErrorCode;
import com.example.skunivProject.domain.recruit.repository.RecruitApplyRepository;
import com.example.skunivProject.domain.recruit.repository.RecruitPostRepository;
import com.example.skunivProject.domain.team.dto.ResponseDto;
import com.example.skunivProject.domain.team.entity.Team;
import com.example.skunivProject.domain.team.enums.CreateType;
import com.example.skunivProject.domain.team.enums.Status;
import com.example.skunivProject.domain.team.exception.TeamException;
import com.example.skunivProject.domain.team.exception.code.TeamErrorCode;
import com.example.skunivProject.domain.team.repository.TeamRepository;
import com.example.skunivProject.domain.teammember.entity.TeamMember;
import com.example.skunivProject.domain.teammember.enums.Role;
import com.example.skunivProject.domain.teammember.repository.TeamMemberRepository;
import com.example.skunivProject.domain.users.entity.Users;
import com.example.skunivProject.domain.users.exception.UserException;
import com.example.skunivProject.domain.users.exception.code.UserErrorCode;
import com.example.skunivProject.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final RecruitPostRepository recruitPostRepository;
    private final RecruitApplyRepository recruitApplyRepository;
    private final UserRepository userRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public Team createTeamFromRecruit(Long postId, String username) {
        Users currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        RecruitPost recruitPost = recruitPostRepository.findById(postId)
                .orElseThrow(() -> new RecruitException(RecruitErrorCode.RECRUIT_POST_NOT_FOUND));

        if (!recruitPost.getWriter().getId().equals(currentUser.getId())) {
            throw new RecruitException(RecruitErrorCode.FORBIDDEN_TO_UPDATE_STATUS);
        }
        if (recruitPost.getStatus() != PostStatus.CLOSED) {
            throw new RecruitException(RecruitErrorCode.POST_NOT_CLOSED);
        }
        if (teamRepository.existsByRecruitPost(recruitPost)) {
            throw new RecruitException(RecruitErrorCode.ALREADY_TEAM_CREATED);
        }

        Team newTeam = Team.builder()
                .recruitPost(recruitPost)
                .competition(recruitPost.getCompetition())
                .createType(CreateType.DIRECT)
                .status(Status.COMPLETE)
                .build();

        TeamMember leader = TeamMember.builder()
                .user(recruitPost.getWriter())
                .role(Role.LEADER)
                .build();
        newTeam.addMember(leader);

        List<RecruitApply> acceptedApplies = recruitApplyRepository.findAllByRecruitPostAndStatus(recruitPost, ApplyStatus.ACCEPTED);
        for (RecruitApply apply : acceptedApplies) {
            TeamMember member = TeamMember.builder()
                    .user(apply.getUser())
                    .role(Role.MEMBER)
                    .build();
            newTeam.addMember(member);
        }

        return teamRepository.save(newTeam);
    }

    @Transactional(readOnly = true)
    public List<ResponseDto.MyTeamDto> getMyTeams(String username) {
        Users currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        List<TeamMember> myTeamMemberships = teamMemberRepository.findAllByUser(currentUser);

        return myTeamMemberships.stream()
                .map(TeamMember::getTeam)
                .distinct()
                .filter(team -> team.getStatus() == Status.COMPLETE)
                .map(team -> {
                    List<String> memberNames = team.getMembers().stream()
                            .map(member -> member.getUser().getName())
                            .collect(Collectors.toList());

                    String roomStatus = chatRoomRepository.findByTeamId(team.getId())
                            .map(chatRoom -> chatRoom.getId())
                            .orElse("채팅방이 개설되어있지 않습니다.");

                    return ResponseDto.MyTeamDto.builder()
                            .userId(currentUser.getId())
                            .teamId(team.getId())
                            .competitionTitle(team.getCompetition().getTitle())
                            .memberNames(memberNames)
                            .roomStatus(roomStatus)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void finishTeam(Long teamId, String username) {
        Users currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamException(TeamErrorCode.TEAM_NOT_FOUND));

        boolean isLeader = team.getMembers().stream()
                .anyMatch(member -> member.getRole() == Role.LEADER && member.getUser().getId().equals(currentUser.getId()));
        if (!isLeader) {
            throw new TeamException(TeamErrorCode.FORBIDDEN_TO_FINISH_TEAM);
        }
        if (team.getStatus() != Status.COMPLETE) {
            throw new TeamException(TeamErrorCode.TEAM_ALREADY_FINISHED);
        }

        if (team.getCompetition().getApplyEnd().isBefore(LocalDateTime.now())) {
            team.setStatus(Status.FINISHED);
            for (TeamMember member : team.getMembers()) {
                member.getUser().addPointsAndRankUp(50);
            }
        } else {
            team.setStatus(Status.ABANDONED);
        }

        chatRoomRepository.findByTeamId(teamId).ifPresent(chatRoom -> {
            chatMessageRepository.deleteAllByRoomId(chatRoom.getId());
            chatRoomRepository.delete(chatRoom);
        });
    }

    @Transactional(readOnly = true)
    public List<ResponseDto.MyFinishedCompetitionDto> getMyFinishedTeams(String username) {
        Users currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        List<TeamMember> myTeamMemberships = teamMemberRepository.findAllByUser(currentUser);

        return myTeamMemberships.stream()
                .map(TeamMember::getTeam)
                .distinct()
                .filter(team -> team.getStatus() == Status.FINISHED)
                .map(team -> {
                    List<String> memberNames = team.getMembers().stream()
                            .map(member -> member.getUser().getName())
                            .collect(Collectors.toList());

                    return ResponseDto.MyFinishedCompetitionDto.builder()
                            .competitionTitle(team.getCompetition().getTitle())
                            .imgUrl(team.getCompetition().getImgUrl())
                            .memberNames(memberNames)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
