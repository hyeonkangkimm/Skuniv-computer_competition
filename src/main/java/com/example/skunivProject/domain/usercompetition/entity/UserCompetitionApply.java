package com.example.skunivProject.domain.usercompetition.entity;

import com.example.skunivProject.domain.usercompetition.enums.Status;
import com.example.skunivProject.domain.usercompetition.enums.Type;
import com.example.skunivProject.global.baseentity.BaseIdEntity;
import com.example.skunivProject.domain.users.entity.Users;
import com.example.skunivProject.domain.competition.entity.Competition;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "user_competition_apply")
public class UserCompetitionApply extends BaseIdEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_id")
    private Competition competition;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.APPLIED;



}
