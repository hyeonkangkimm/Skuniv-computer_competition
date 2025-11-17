package com.example.skunivProject.domain.recruit.repository;

import com.example.skunivProject.domain.competition.entity.Competition; // Competition import 추가
import com.example.skunivProject.domain.recruit.entity.RecruitPost;
import com.example.skunivProject.domain.recruit.enums.PostStatus;
import com.example.skunivProject.domain.users.entity.Users;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitPostRepository extends JpaRepository<RecruitPost, Long> {

    /**
     * 특정 사용자가 작성한 모든 모집공고를 정렬하여 조회
     */
    List<RecruitPost> findAllByWriter(Users writer, Sort sort);


    /**
     * 특정 공모전(Competition)에 속하고 특정 상태인 모집공고를 정렬하여 조회
     */
    List<RecruitPost> findAllByCompetitionAndStatus(Competition competition, PostStatus status, Sort sort);
}
