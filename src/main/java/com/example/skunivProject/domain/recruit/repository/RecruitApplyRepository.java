package com.example.skunivProject.domain.recruit.repository;

import com.example.skunivProject.domain.recruit.entity.RecruitApply;
import com.example.skunivProject.domain.recruit.entity.RecruitPost;
import com.example.skunivProject.domain.recruit.enums.ApplyStatus;
import com.example.skunivProject.domain.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
