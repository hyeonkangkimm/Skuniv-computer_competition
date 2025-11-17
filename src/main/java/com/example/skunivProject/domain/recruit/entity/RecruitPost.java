package com.example.skunivProject.domain.recruit.entity;

import com.example.skunivProject.domain.recruit.enums.PostStatus;
import com.example.skunivProject.global.baseentity.BaseIdEntity;
import com.example.skunivProject.domain.users.entity.Users;
import com.example.skunivProject.domain.competition.entity.Competition;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "recruit_post")
public class RecruitPost extends BaseIdEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_id")
    private Competition competition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Users writer;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status = PostStatus.OPEN;

    @OneToMany(mappedBy = "recruitPost")
    private List<RecruitApply> applies;

    @Column(nullable = false)
    private int maxCapacity;


}
