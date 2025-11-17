package com.example.skunivProject.domain.recruit.repository;

import com.example.skunivProject.domain.recruit.entity.RecruitPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitPostRepository extends JpaRepository<RecruitPost, Long> {
}
