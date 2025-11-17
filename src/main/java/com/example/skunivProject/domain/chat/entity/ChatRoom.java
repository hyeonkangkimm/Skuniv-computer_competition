package com.example.skunivProject.domain.chat.entity;

import com.example.skunivProject.global.baseentity.BaseIdEntity;
import com.example.skunivProject.domain.team.entity.Team;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "chat_room")
public class ChatRoom extends BaseIdEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @OneToMany(mappedBy = "room")
    private List<ChatMessage> messages;



}
