package com.example.skunivProject.domain.competition.repository;

import com.example.skunivProject.domain.competition.entity.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    // isDeleted = false인 데이터만 조회
    List<Competition> findAllByIsDeletedFalse();
}
