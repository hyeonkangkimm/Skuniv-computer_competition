package com.example.skunivProject.domain.team.entity;

import com.example.skunivProject.domain.team.enums.CreateType;
import com.example.skunivProject.domain.team.enums.Status;
import com.example.skunivProject.domain.teammember.entity.TeamMember;
import com.example.skunivProject.global.baseentity.BaseIdEntity;
import com.example.skunivProject.domain.competition.entity.Competition;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "team")
public class Team extends BaseIdEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_id")
    private Competition competition;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CreateType createType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.FORMING;





    @OneToMany(mappedBy = "team")
    private List<TeamMember> members;



}
