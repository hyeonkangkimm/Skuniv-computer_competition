package com.example.skunivProject.domain.recruit.repository;

import com.example.skunivProject.domain.recruit.entity.RecruitPost;
import com.example.skunivProject.domain.users.entity.Users;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecruitPostRepository extends JpaRepository<RecruitPost, Long> {
    /**
     * 특정 사용자가 작성한 모든 모집공고를 조회
     */
    List<RecruitPost> findAllByWriter(Users writer);

    /**
     * 특정 사용자가 작성한 모든 모집공고를 정렬하여 조회
     */
    List<RecruitPost> findAllByWriter(Users writer, Sort sort);
}
