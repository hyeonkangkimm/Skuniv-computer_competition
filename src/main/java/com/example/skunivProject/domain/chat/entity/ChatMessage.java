package com.example.skunivProject.domain.chat.entity;

import com.example.skunivProject.global.baseentity.BaseMongoEntity;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Document(collection = "chat_messages")
public class ChatMessage extends BaseMongoEntity {

    private String roomId;
    private Long userId;
    private String message;
    private boolean isRead = false;

    private String username;
    private String name;

    public void markAsRead() {
        this.isRead = true;
    }
}
