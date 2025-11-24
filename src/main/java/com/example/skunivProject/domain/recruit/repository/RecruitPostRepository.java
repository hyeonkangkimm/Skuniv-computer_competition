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

    List<RecruitPost> findAllByCompetitionAndStatusIn(Competition competition, Collection<PostStatus> statuses, Sort sort);

    /**
     * [수정] 특정 공모전 내에서 1인 참가가 가능하고, 모집 중인 모든 공고를 조회합니다.
     */
    List<RecruitPost> findAllByCompetitionAndStatusAndRandomApplySetting(Competition competition, PostStatus status, RandomApplySetting randomApplySetting);

    /**
     * [추가] 특정 공모전(Competition)에 특정 작성자(Writer)가 작성한 모집글이 있는지 확인
     */
    boolean existsByCompetitionAndWriter(Competition competition, Users writer);
}
