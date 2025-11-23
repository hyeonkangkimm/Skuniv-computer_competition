package com.example.skunivProject.domain.recruit.repository;

import com.example.skunivProject.domain.competition.entity.Competition;
import com.example.skunivProject.domain.recruit.entity.RecruitPost;
import com.example.skunivProject.domain.recruit.enums.PostStatus;
import com.example.skunivProject.domain.recruit.enums.RandomApplySetting;
import com.example.skunivProject.domain.users.entity.Users;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RecruitPostRepository extends JpaRepository<RecruitPost, Long> {

    List<RecruitPost> findAllByWriter(Users writer, Sort sort);

    List<RecruitPost> findAllByCompetitionAndStatus(Competition competition, PostStatus status, Sort sort);

    List<RecruitPost> findAllByWriterAndStatusIn(Users writer, Collection<PostStatus> statuses, Sort sort);

    List<RecruitPost> findAllByStatusAndRandomApplySetting(PostStatus status, RandomApplySetting randomApplySetting);

    /**
     * 특정 공모전에 속하고, 주어진 상태 목록(예: OPEN, CLOSED)에 포함되는 모든 모집공고를 정렬하여 조회
     */
    List<RecruitPost> findAllByCompetitionAndStatusIn(Competition competition, Collection<PostStatus> statuses, Sort sort);
}
