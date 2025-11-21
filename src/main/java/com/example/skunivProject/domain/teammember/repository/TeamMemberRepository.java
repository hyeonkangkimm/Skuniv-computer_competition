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

    Optional<TeamMember> findByTeamAndRole(Team team, Role role);

    @EntityGraph(attributePaths = {"team", "team.competition", "team.members", "team.members.user"})
    List<TeamMember> findAllByUser(Users user);
}
