package com.example.skunivProject.domain.chat.entity;

import com.example.skunivProject.global.baseentity.BaseIdEntity;
import com.example.skunivProject.domain.users.entity.Users;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "chat_message")
public class ChatMessage extends BaseIdEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    private boolean isRead = false; // 읽음 처리는 메시지에만 필요한 상태이므로 여기에 유지

    public void markAsRead(){
        this.isRead = true;
    }
}
