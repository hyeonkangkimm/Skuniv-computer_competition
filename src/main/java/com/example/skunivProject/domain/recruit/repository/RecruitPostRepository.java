package com.example.skunivProject.domain.recruit.repository;

import com.example.skunivProject.domain.competition.entity.Competition;
import com.example.skunivProject.domain.recruit.entity.RecruitPost;
import com.example.skunivProject.domain.recruit.enums.PostStatus;
import com.example.skunivProject.domain.recruit.enums.RandomApplySetting;
import com.example.skunivProject.domain.users.entity.Users;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitPostRepository extends JpaRepository<RecruitPost, Long> {

    List<RecruitPost> findAllByWriter(Users writer, Sort sort);

    List<RecruitPost> findAllByCompetitionAndStatus(Competition competition, PostStatus status, Sort sort);

    List<RecruitPost> findAllByWriterAndStatus(Users writer, PostStatus status, Sort sort);

    /**
     * 1인 참가가 가능하고, 모집 중인 모든 공고를 조회합니다.
     */
    List<RecruitPost> findAllByStatusAndRandomApplySetting(PostStatus status, RandomApplySetting randomApplySetting);
}
