package com.example.skunivProject.domain.team.service;

import com.example.skunivProject.domain.recruit.entity.RecruitApply;
import com.example.skunivProject.domain.recruit.entity.RecruitPost;
import com.example.skunivProject.domain.recruit.enums.ApplyStatus;
import com.example.skunivProject.domain.recruit.enums.PostStatus;
import com.example.skunivProject.domain.recruit.exception.RecruitException;
import com.example.skunivProject.domain.recruit.exception.code.RecruitErrorCode;
import com.example.skunivProject.domain.recruit.repository.RecruitApplyRepository;
import com.example.skunivProject.domain.recruit.repository.RecruitPostRepository;
import com.example.skunivProject.domain.team.entity.Team;
import com.example.skunivProject.domain.team.enums.CreateType;
import com.example.skunivProject.domain.team.repository.TeamRepository;
import com.example.skunivProject.domain.teammember.entity.TeamMember;
import com.example.skunivProject.domain.teammember.enums.Role;
import com.example.skunivProject.domain.users.entity.Users;
import com.example.skunivProject.domain.users.exception.UserException;
import com.example.skunivProject.domain.users.exception.code.UserErrorCode;
import com.example.skunivProject.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final RecruitPostRepository recruitPostRepository;
    private final RecruitApplyRepository recruitApplyRepository;
    private final UserRepository userRepository;

    @Transactional
    public Team createTeamFromRecruit(Long postId, String username) {
        // 1. 사용자 및 모집공고 조회
        Users currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        RecruitPost recruitPost = recruitPostRepository.findById(postId)
                .orElseThrow(() -> new RecruitException(RecruitErrorCode.RECRUIT_POST_NOT_FOUND));

        // 2. 권한 및 상태 검사
        if (!recruitPost.getWriter().getId().equals(currentUser.getId())) {
            throw new RecruitException(RecruitErrorCode.FORBIDDEN_TO_UPDATE_STATUS); // 권한 없음
        }
        if (recruitPost.getStatus() != PostStatus.CLOSED) {
            throw new RecruitException(RecruitErrorCode.POST_NOT_CLOSED);
        }
        if (teamRepository.existsByRecruitPost(recruitPost)) {
            throw new RecruitException(RecruitErrorCode.ALREADY_TEAM_CREATED);
        }

        // 3. 새로운 Team 객체 생성
        Team newTeam = Team.builder()
                .recruitPost(recruitPost)
                .competition(recruitPost.getCompetition())
                .createType(CreateType.DIRECT) // 직접 모집을 통한 생성
                .status(com.example.skunivProject.domain.team.enums.Status.COMPLETE) // 팀 구성 완료 상태
                .build();

        // 4. 공고 작성자를 LEADER로 팀에 추가
        TeamMember leader = TeamMember.builder()
                .user(recruitPost.getWriter())
                .role(Role.LEADER)
                .build();
        newTeam.addMember(leader);

        // 5. 수락된(ACCEPTED) 지원자들을 MEMBER로 팀에 추가
        List<RecruitApply> acceptedApplies = recruitApplyRepository.findAllByRecruitPostAndStatus(recruitPost, ApplyStatus.ACCEPTED);
        for (RecruitApply apply : acceptedApplies) {
            TeamMember member = TeamMember.builder()
                    .user(apply.getUser())
                    .role(Role.MEMBER)
                    .build();
            newTeam.addMember(member);
        }

        // 6. 팀 저장 (Cascade 설정으로 TeamMember들도 함께 저장됨)
        return teamRepository.save(newTeam);
    }
}
