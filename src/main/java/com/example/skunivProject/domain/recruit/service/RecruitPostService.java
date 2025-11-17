package com.example.skunivProject.domain.recruit.service;

import com.example.skunivProject.domain.recruit.Dto.RequestDto;
import com.example.skunivProject.domain.recruit.Dto.ResponseDto;
import com.example.skunivProject.domain.recruit.converter.RecruitPostConverter;
import com.example.skunivProject.domain.recruit.entity.RecruitPost;
import com.example.skunivProject.domain.recruit.exception.RecruitException;
import com.example.skunivProject.domain.recruit.exception.code.RecruitErrorCode;
import com.example.skunivProject.domain.recruit.repository.RecruitPostRepository;
import com.example.skunivProject.domain.competition.entity.Competition;
import com.example.skunivProject.domain.competition.repository.CompetitionRepository;
import com.example.skunivProject.domain.users.entity.Users;
import com.example.skunivProject.domain.users.enums.Rank;
import com.example.skunivProject.domain.users.exception.UserException;
import com.example.skunivProject.domain.users.exception.code.UserErrorCode;
import com.example.skunivProject.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecruitPostService {

    private final RecruitPostRepository recruitPostRepository;
    private final CompetitionRepository competitionRepository;
    private final UserRepository userRepository;
    private final RecruitPostConverter converter;

    @Transactional
    public ResponseDto.RecruitResDto createRecruitPost(RequestDto.RecruitReqDto dto, String username) {

        // 1. 전달받은 username으로 DB에서 완전한 Users 엔티티 조회
        Users writer = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // 2. Rank 체크
        if (writer.getRank() != Rank.RECRUIT) {
            throw new RecruitException(RecruitErrorCode.RECRUIT_NOT_ALLOWED);
        }

        // 3. Competition 조회
        Competition competition = competitionRepository.findById(dto.getCompetitionId())
                .orElseThrow(() -> new RecruitException(RecruitErrorCode.COMPETITION_NOT_FOUND));

        // 4. DTO → Entity 변환
        RecruitPost recruitPost = converter.toEntity(dto, competition, writer);

        // 5. 저장
        recruitPostRepository.save(recruitPost);

        // 6. Entity → Response DTO 변환
        return converter.toResponseDto(recruitPost);
    }
}
