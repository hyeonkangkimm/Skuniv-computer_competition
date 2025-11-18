package com.example.skunivProject.domain.chat.entity;

import com.example.skunivProject.global.baseentity.BaseMongoEntity;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Document(collection = "chat_messages")
public class ChatMessage extends BaseMongoEntity {

    private String roomId; // ChatRoom 컬렉션 참조 ID
    private Long userId; // SQL Users PK 참조

    private String message;
    private boolean isRead = false;

    public void markAsRead() {
        this.isRead = true;
    }
}
