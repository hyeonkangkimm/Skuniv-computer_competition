package com.example.skunivProject.domain.competition.repository;

import com.example.skunivProject.domain.competition.entity.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    // isDeleted = false인 데이터만 조회
    List<Competition> findAllByIsDeletedFalse();

    /**
     * 삭제되지 않았고, 접수 마감일이 현재 시간 이후인 공모전만 조회
     */
    List<Competition> findAllByIsDeletedFalseAndApplyEndAfter(LocalDateTime now);
}
