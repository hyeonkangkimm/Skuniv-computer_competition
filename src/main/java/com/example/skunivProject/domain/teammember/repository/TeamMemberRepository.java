package com.example.skunivProject.domain.teammember.repository;

import com.example.skunivProject.domain.team.entity.Team;
import com.example.skunivProject.domain.teammember.entity.TeamMember;
import com.example.skunivProject.domain.teammember.enums.Role;
import com.example.skunivProject.domain.users.entity.Users;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    /**
     * 특정 팀의 특정 역할을 가진 멤버를 조회 (주로 리더를 찾을 때 사용)
     */
    Optional<TeamMember> findByTeamAndRole(Team team, Role role);

    /**
     * 특정 사용자가 속한 모든 팀 멤버 정보를 조회 (N+1 방지를 위해 team과 competition 정보 함께 fetch)
     */
    @EntityGraph(attributePaths = {"team", "team.competition"})
    List<TeamMember> findAllByUser(Users user);
}
