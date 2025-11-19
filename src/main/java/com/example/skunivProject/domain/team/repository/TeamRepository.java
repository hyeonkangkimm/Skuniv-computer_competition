package com.example.skunivProject.domain.team.repository;

import com.example.skunivProject.domain.recruit.entity.RecruitPost;
import com.example.skunivProject.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    /**
     * 특정 모집공고로 생성된 팀이 있는지 확인
     */
    boolean existsByRecruitPost(RecruitPost recruitPost);
}
