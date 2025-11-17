package com.example.skunivProject.domain.recruit.entity;

import com.example.skunivProject.domain.recruit.enums.ApplyStatus;
import com.example.skunivProject.global.baseentity.BaseIdEntity;
import com.example.skunivProject.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "recruit_apply", uniqueConstraints = @UniqueConstraint(columnNames = {"recruit_post_id","user_id"}))
public class RecruitApply extends BaseIdEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruit_post_id")
    private RecruitPost recruitPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplyStatus status = ApplyStatus.APPLIED;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;






}
