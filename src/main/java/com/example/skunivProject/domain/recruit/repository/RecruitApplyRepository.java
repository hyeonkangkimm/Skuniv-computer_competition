package com.example.skunivProject.domain.recruit.repository;

import com.example.skunivProject.domain.recruit.entity.RecruitApply;
import com.example.skunivProject.domain.recruit.entity.RecruitPost;
import com.example.skunivProject.domain.recruit.enums.ApplyStatus;
import com.example.skunivProject.domain.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecruitApplyRepository extends JpaRepository<RecruitApply, Long> {

    /**
     * 특정 모집공고에 특정 사용자가 지원했는지 확인
     */
    boolean existsByRecruitPostAndUser(RecruitPost recruitPost, Users user);

    /**
     * 특정 모집공고에 대해 특정 상태(예: ACCEPTED)인 지원자 수를 계산
     */
    long countByRecruitPostAndStatus(RecruitPost recruitPost, ApplyStatus status);

    /**
     * 특정 모집공고에 대한 모든 지원서 목록을 조회
     */
    List<RecruitApply> findAllByRecruitPost(RecruitPost recruitPost);

    /**
     * 특정 모집공고에 대해 특정 상태인 지원서 목록을 조회
     */
    List<RecruitApply> findAllByRecruitPostAndStatus(RecruitPost recruitPost, ApplyStatus status);

    /**
     * 특정 공모전(Competition)에 특정 사용자가 지원했는지 확인
     */
    @Query("SELECT COUNT(ra) > 0 FROM RecruitApply ra WHERE ra.recruitPost.competition.id = :competitionId AND ra.user.id = :userId")
    boolean existsByCompetitionIdAndUserId(@Param("competitionId") Long competitionId, @Param("userId") Long userId);
}
