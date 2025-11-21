package com.example.skunivProject.domain.recruit.service;

import com.example.skunivProject.domain.recruit.Dto.RequestDto;
import com.example.skunivProject.domain.recruit.Dto.ResponseDto;
import com.example.skunivProject.domain.recruit.converter.RecruitPostConverter;
import com.example.skunivProject.domain.recruit.entity.RecruitApply;
import com.example.skunivProject.domain.recruit.entity.RecruitPost;
import com.example.skunivProject.domain.recruit.enums.ApplyStatus;
import com.example.skunivProject.domain.recruit.enums.PostStatus;
import com.example.skunivProject.domain.recruit.enums.RandomApplySetting;
import com.example.skunivProject.domain.recruit.exception.RecruitException;
import com.example.skunivProject.domain.recruit.exception.code.RecruitErrorCode;
import com.example.skunivProject.domain.recruit.repository.RecruitApplyRepository;
import com.example.skunivProject.domain.recruit.repository.RecruitPostRepository;
import com.example.skunivProject.domain.competition.entity.Competition;
import com.example.skunivProject.domain.competition.repository.CompetitionRepository;
import com.example.skunivProject.domain.users.entity.Users;
import com.example.skunivProject.domain.users.exception.UserException;
import com.example.skunivProject.domain.users.exception.code.UserErrorCode;
import com.example.skunivProject.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;
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

        List<RecruitPost> myOpenPosts = recruitPostRepository.findAllByWriterAndStatus(
                currentUser, PostStatus.OPEN, Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return myOpenPosts.stream()
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
        ApplyStatus newStatus = dto.getStatus();
        ApplyStatus currentStatus = apply.getStatus();

        if (!recruitPost.getWriter().getId().equals(writer.getId())) {
            throw new RecruitException(RecruitErrorCode.FORBIDDEN_TO_UPDATE_STATUS);
        }

        if (currentStatus == ApplyStatus.ACCEPTED) {
            throw new RecruitException(RecruitErrorCode.CANNOT_CHANGE_ACCEPTED_STATUS);
        }
        if (newStatus == ApplyStatus.APPLIED) {
            throw new RecruitException(RecruitErrorCode.CANNOT_UPDATE_TO_APPLIED);
        }
        if (currentStatus == newStatus) {
            throw new RecruitException(RecruitErrorCode.SAME_STATUS_REQUEST);
        }

        if (newStatus == ApplyStatus.ACCEPTED) {
            long acceptedCount = recruitApplyRepository.countByRecruitPostAndStatus(recruitPost, ApplyStatus.ACCEPTED);
            if (acceptedCount >= recruitPost.getMaxCapacity()) {
                throw new RecruitException(RecruitErrorCode.CAPACITY_EXCEEDED);
            }
        }

        apply.setStatus(newStatus);

        long finalAcceptedCount = recruitApplyRepository.countByRecruitPostAndStatus(recruitPost, ApplyStatus.ACCEPTED);
        if (finalAcceptedCount >= recruitPost.getMaxCapacity()) {
            recruitPost.setStatus(PostStatus.CLOSED);
        } else {
            recruitPost.setStatus(PostStatus.OPEN);
        }

        return ResponseDto.UpdateApplyStatus.builder()
                .applyId(apply.getId())
                .status(apply.getStatus())
                .build();
    }

    public List<ResponseDto.RecruitPostSummaryDto> getOpenRecruitPosts(Long competitionId) {
        Competition competition = competitionRepository.findById(competitionId)
                .orElseThrow(() -> new RecruitException(RecruitErrorCode.COMPETITION_NOT_FOUND));

        List<RecruitPost> openPosts = recruitPostRepository.findAllByCompetitionAndStatus(competition, PostStatus.OPEN, Sort.by(Sort.Direction.DESC, "createdAt"));

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

    @Transactional
    public void deleteRecruitPost(Long postId, String username) {
        Users currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        RecruitPost recruitPost = recruitPostRepository.findById(postId)
                .orElseThrow(() -> new RecruitException(RecruitErrorCode.RECRUIT_POST_NOT_FOUND));

        if (!recruitPost.getWriter().getId().equals(currentUser.getId())) {
            throw new RecruitException(RecruitErrorCode.FORBIDDEN_TO_DELETE);
        }
        if (recruitPost.getStatus() != PostStatus.OPEN) {
            throw new RecruitException(RecruitErrorCode.CANNOT_DELETE_CLOSED_POST);
        }

        recruitPostRepository.delete(recruitPost);
    }

    @Transactional
    public ResponseDto.RandomApplyResult randomApply(RequestDto.RandomApply dto, String username) {
        Users applicant = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        List<RecruitPost> availablePosts = recruitPostRepository.findAllByStatusAndRandomApplySetting(PostStatus.OPEN, RandomApplySetting.ALLOW);

        List<RecruitPost> filteredPosts = availablePosts.stream()
                .filter(post -> !post.getWriter().getId().equals(applicant.getId()))
                .filter(post -> !recruitApplyRepository.existsByRecruitPostAndUser(post, applicant))
                .collect(Collectors.toList());

        if (filteredPosts.isEmpty()) {
            throw new RecruitException(RecruitErrorCode.NO_AVAILABLE_RANDOM_POST);
        }

        List<RecruitPost> postsWithRemainingCapacity = filteredPosts.stream()
                .filter(post -> {
                    long acceptedCount = recruitApplyRepository.countByRecruitPostAndStatus(post, ApplyStatus.ACCEPTED);
                    return post.getMaxCapacity() > acceptedCount;
                })
                .collect(Collectors.toList());

        if (postsWithRemainingCapacity.isEmpty()) {
            throw new RecruitException(RecruitErrorCode.NO_AVAILABLE_RANDOM_POST);
        }

        long minRemainingCapacity = postsWithRemainingCapacity.stream()
                .mapToLong(post -> post.getMaxCapacity() - recruitApplyRepository.countByRecruitPostAndStatus(post, ApplyStatus.ACCEPTED))
                .min()
                .orElse(Long.MAX_VALUE);

        List<RecruitPost> bestFitPosts = postsWithRemainingCapacity.stream()
                .filter(post -> (post.getMaxCapacity() - recruitApplyRepository.countByRecruitPostAndStatus(post, ApplyStatus.ACCEPTED)) == minRemainingCapacity)
                .collect(Collectors.toList());

        RecruitPost targetPost = bestFitPosts.get(new Random().nextInt(bestFitPosts.size()));

        RecruitApply newApply = RecruitApply.builder()
                .recruitPost(targetPost)
                .user(applicant)
                .content(dto.getContent())
                .status(ApplyStatus.ACCEPTED)
                .build();
        recruitApplyRepository.save(newApply);

        long finalAcceptedCount = recruitApplyRepository.countByRecruitPostAndStatus(targetPost, ApplyStatus.ACCEPTED);
        if (finalAcceptedCount >= targetPost.getMaxCapacity()) {
            targetPost.setStatus(PostStatus.CLOSED);
        }

        return ResponseDto.RandomApplyResult.builder()
                .appliedPostId(targetPost.getId())
                .postTitle(targetPost.getTitle())
                .writerName(targetPost.getWriter().getName())
                .build();
    }
}
