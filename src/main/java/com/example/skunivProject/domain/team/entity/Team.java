package com.example.skunivProject.domain.team.entity;

import com.example.skunivProject.domain.recruit.entity.RecruitPost;
import com.example.skunivProject.domain.team.enums.CreateType;
import com.example.skunivProject.domain.team.enums.Status;
import com.example.skunivProject.domain.teammember.entity.TeamMember;
import com.example.skunivProject.global.baseentity.BaseIdEntity;
import com.example.skunivProject.domain.competition.entity.Competition;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "team")
public class Team extends BaseIdEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruit_post_id", unique = true)
    private RecruitPost recruitPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_id")
    private Competition competition;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CreateType createType;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.FORMING;

    @Builder.Default
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamMember> members = new ArrayList<>();

    //== 연관관계 편의 메소드 ==//
    public void addMember(TeamMember teamMember) {
        members.add(teamMember);
        teamMember.setTeam(this);
    }
}
