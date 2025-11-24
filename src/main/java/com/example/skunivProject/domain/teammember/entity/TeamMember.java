package com.example.skunivProject.domain.teammember.entity;

import com.example.skunivProject.domain.team.entity.Team;
import com.example.skunivProject.domain.teammember.enums.Role;
import com.example.skunivProject.domain.users.entity.Users;
import com.example.skunivProject.global.baseentity.BaseIdEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "team_member", uniqueConstraints = @UniqueConstraint(columnNames = {"team_id", "user_id"}))
public class TeamMember extends BaseIdEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.MEMBER;

    public void changeRole(Role newRole) {
        this.role = newRole;
    }

}
