package com.example.skunivProject.domain.competition.entity;

import com.example.skunivProject.domain.recruit.entity.RecruitPost;
import com.example.skunivProject.domain.team.entity.Team;
import com.example.skunivProject.domain.usercompetition.entity.UserCompetitionApply;
import com.example.skunivProject.global.baseentity.BaseIdEntity;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "competition")
public class Competition extends BaseIdEntity {

    //제목
    @Column(nullable = false)
    private String title;

    //응모분야
    @Column(nullable = false)
    private String category;

    //주최
    @Column(nullable = false)
    private String host;

    //주최사 홈페이지
    @Column(nullable = false)
    private String homePage;

    //씽굿 홈페이지
    @Column(nullable = false)
    private String thinkGoodLink;

    //주관
    @Column(nullable = true)
    private String organizer;

    //이미지 url
    @Column(nullable = false)
    private String imgUrl;

    //접수시작
    @Column(nullable = false)
    private LocalDateTime applyStart;

    //접수종료
    @Column(nullable = false)
    private LocalDateTime applyEnd;

    //참가자격
    @Column(nullable = false)
    private String qualification;

    //시상금
    @Column(nullable = false)
    private String awardFirst;

    @OneToMany(mappedBy = "competition")
    private List<UserCompetitionApply> applies;

    @OneToMany(mappedBy = "competition")
    private List<Team> teams;

    @OneToMany(mappedBy = "competition")
    private List<RecruitPost> recruitPosts;
}
