package com.example.skunivProject.domain.recruit.service;

import com.example.skunivProject.domain.recruit.Dto.RequestDto;
import com.example.skunivProject.domain.recruit.Dto.ResponseDto;
import com.example.skunivProject.domain.recruit.converter.RecruitPostConverter;
import com.example.skunivProject.domain.recruit.entity.RecruitApply;
import com.example.skunivProject.domain.recruit.entity.RecruitPost;
import com.example.skunivProject.domain.recruit.enums.ApplyStatus;
import com.example.skunivProject.domain.recruit.enums.PostStatus;
import com.example.skunivProject.domain.recruit.exception.RecruitException;
import com.example.skunivProject.domain.recruit.exception.code.RecruitErrorCode;
import com.example.skunivProject.domain.recruit.repository.RecruitApplyRepository;
import com.example.skunivProject.domain.recruit.repository.RecruitPostRepository;
import com.example.skunivProject.domain.competition.entity.Competition;
import com.example.skunivProject.domain.competition.repository.CompetitionRepository;
import com.example.skunivProject.domain.users.entity.Users;
import com.example.skunivProject.domain.users.enums.Rank;
import com.example.skunivProject.domain.users.exception.UserException;
import com.example.skunivProject.domain.users.exception.code.UserErrorCode;
import com.example.skunivProject.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitPostService {

    private final RecruitPostRepository recruitPostRepository;
    private final CompetitionRepository competitionRepository;
    private final UserRepository userRepository;
    private final RecruitApplyRepository recruitApplyRepository;
    private final RecruitPostConverter converter;

    @Transactional
    public ResponseDto.RecruitResDto createRecruitPost(RequestDto.RecruitReqDto dto, String username) {
        Users writer = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        if (writer.getRank() != Rank.TREE) {
            throw new RecruitException(RecruitErrorCode.RECRUIT_NOT_ALLOWED);
        }

        Competition competition = competitionRepository.findById(dto.getCompetitionId())
                .orElseThrow(() -> new RecruitException(RecruitErrorCode.COMPETITION_NOT_FOUND));

        if (competition.isDeleted()) {
            throw new RecruitException(RecruitErrorCode.COMPETITION_DELETED);
        }

        RecruitPost recruitPost = converter.toEntity(dto, competition, writer);
        recruitPostRepository.save(recruitPost);
        return converter.toResponseDto(recruitPost);
    }

    @Transactional
    public ResponseDto.Apply applyToRecruitPost(Long postId, RequestDto.Apply dto, String username) {
        Users applicant = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        RecruitPost recruitPost = recruitPostRepository.findById(postId)
                .orElseThrow(() -> new RecruitException(RecruitErrorCode.RECRUIT_POST_NOT_FOUND));

        if (recruitPost.getWriter().getId().equals(applicant.getId())) {
            throw new RecruitException(RecruitErrorCode.CANNOT_APPLY_TO_OWN_POST);
        }

        if (recruitApplyRepository.existsByRecruitPostAndUser(recruitPost, applicant)) {
            throw new RecruitException(RecruitErrorCode.ALREADY_APPLIED);
        }

        long acceptedCount = recruitApplyRepository.countByRecruitPostAndStatus(recruitPost, ApplyStatus.ACCEPTED);
        if (acceptedCount >= recruitPost.getMaxCapacity()) {
            throw new RecruitException(RecruitErrorCode.CAPACITY_EXCEEDED);
        }

        RecruitApply newApply = RecruitApply.builder()
                .recruitPost(recruitPost)
                .user(applicant)
                .content(dto.getContent())
                .status(ApplyStatus.APPLIED)
                .build();

        recruitApplyRepository.save(newApply);

        return ResponseDto.Apply.builder()
                .applyId(newApply.getId())
                .recruitPostId(recruitPost.getId())
                .applicantName(applicant.getName())
                .status(newApply.getStatus())
                .applicantPhone(applicant.getPhone())
                .build();
    }

    public List<ResponseDto.ApplyDetail> getAppliesForPost(Long postId, String username) {
        Users currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        RecruitPost recruitPost = recruitPostRepository.findById(postId)
                .orElseThrow(() -> new RecruitException(RecruitErrorCode.RECRUIT_POST_NOT_FOUND));

        if (!recruitPost.getWriter().getId().equals(currentUser.getId())) {
            throw new RecruitException(RecruitErrorCode.FORBIDDEN_TO_VIEW_APPLIES);
        }

        List<RecruitApply> applies = recruitApplyRepository.findAllByRecruitPost(recruitPost);

        return applies.stream()
                .map(apply -> ResponseDto.ApplyDetail.builder()
                        .applyId(apply.getId())
                        .applicantId(apply.getUser().getId())
                        .applicantName(apply.getUser().getName())
                        .applicantRank(apply.getUser().getRank())
                        .content(apply.getContent())
                        .status(apply.getStatus())
                        .applicantPhone(apply.getUser().getPhone())
                        .build())
                .collect(Collectors.toList());
    }

    public List<ResponseDto.MyPost> getMyRecruitPosts(String username) {
        Users currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        List<RecruitPost> myPosts = recruitPostRepository.findAllByWriter(currentUser, Sort.by(Sort.Direction.DESC, "createdAt"));

        return myPosts.stream()
                .map(converter::toMyPostDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ResponseDto.UpdateApplyStatus updateApplyStatus(Long applyId, RequestDto.UpdateApplyStatus dto, String username) {
        Users writer = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        RecruitApply apply = recruitApplyRepository.findById(applyId)
                .orElseThrow(() -> new RecruitException(RecruitErrorCode.APPLY_NOT_FOUND));
        RecruitPost recruitPost = apply.getRecruitPost();

        if (!recruitPost.getWriter().getId().equals(writer.getId())) {
            throw new RecruitException(RecruitErrorCode.FORBIDDEN_TO_UPDATE_STATUS);
        }

        if (dto.getStatus() == ApplyStatus.APPLIED) {
            throw new RecruitException(RecruitErrorCode.CANNOT_UPDATE_TO_APPLIED);
        }
        if (apply.getStatus() != ApplyStatus.APPLIED) {
            throw new RecruitException(RecruitErrorCode.INVALID_STATUS_UPDATE);
        }

        if (dto.getStatus() == ApplyStatus.ACCEPTED) {
            long acceptedCount = recruitApplyRepository.countByRecruitPostAndStatus(recruitPost, ApplyStatus.ACCEPTED);
            if (acceptedCount >= recruitPost.getMaxCapacity()) {
                throw new RecruitException(RecruitErrorCode.CAPACITY_EXCEEDED);
            }
        }

        apply.setStatus(dto.getStatus());

        if (dto.getStatus() == ApplyStatus.ACCEPTED) {
            long newAcceptedCount = recruitApplyRepository.countByRecruitPostAndStatus(recruitPost, ApplyStatus.ACCEPTED);
            if (newAcceptedCount >= recruitPost.getMaxCapacity()) {
                recruitPost.setStatus(PostStatus.CLOSED);
            }
        }

        return ResponseDto.UpdateApplyStatus.builder()
                .applyId(apply.getId())
                .status(apply.getStatus())
                .build();
    }

    public List<ResponseDto.FinalTeamMember> getFinalTeam(Long postId, String username) {
        Users currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        RecruitPost recruitPost = recruitPostRepository.findById(postId)
                .orElseThrow(() -> new RecruitException(RecruitErrorCode.RECRUIT_POST_NOT_FOUND));

        if (!recruitPost.getWriter().getId().equals(currentUser.getId())) {
            throw new RecruitException(RecruitErrorCode.FORBIDDEN_TO_VIEW_APPLIES);
        }

        if (recruitPost.getStatus() != PostStatus.CLOSED) {
            throw new RecruitException(RecruitErrorCode.POST_NOT_CLOSED);
        }

        List<RecruitApply> acceptedApplies = recruitApplyRepository.findAllByRecruitPostAndStatus(recruitPost, ApplyStatus.ACCEPTED);

        List<ResponseDto.FinalTeamMember> finalTeam = new ArrayList<>(acceptedApplies.stream()
                .map(apply -> {
                    Users member = apply.getUser();
                    return ResponseDto.FinalTeamMember.builder()
                            .userId(member.getId())
                            .name(member.getName())
                            .rank(member.getRank())
                            .phone(member.getPhone())
                            .build();
                })
                .collect(Collectors.toList()));

        Users writer = recruitPost.getWriter();
        finalTeam.add(0, ResponseDto.FinalTeamMember.builder()
                .userId(writer.getId())
                .name(writer.getName())
                .rank(writer.getRank())
                .phone(writer.getPhone())
                .build());

        return finalTeam;
    }

    public List<ResponseDto.RecruitPostSummaryDto> getOpenRecruitPosts(Long competitionId) {
        // 1. competitionId로 Competition 엔티티 조회
        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new RecruitException(RecruitErrorCode.COMPETITION_NOT_FOUND));

        // 2. 해당 Competition에 속하고 OPEN 상태인 모집공고를 최신순으로 조회
        List<RecruitPost> openPosts = recruitPostRepository.findAllByCompetitionAndStatus(competition, PostStatus.OPEN, Sort.by(Sort.Direction.DESC, "createdAt"));

        // 3. DTO로 변환하여 반환
        return openPosts.stream()
                .map(post -> {
                    long currentAcceptedCount = recruitApplyRepository.countByRecruitPostAndStatus(post, ApplyStatus.ACCEPTED);
                    return ResponseDto.RecruitPostSummaryDto.builder()
                            .postId(post.getId())
                            .title(post.getTitle())
                            .content(post.getContent())
                            .writerName(post.getWriter().getName())
                            .status(post.getStatus())
                            .currentAcceptedCount(currentAcceptedCount)
                            .maxCapacity(post.getMaxCapacity())
                            .createdAt(post.getCreatedAt())
                            .build();
                })
                .collect(Collectors.toList());
    }
}
