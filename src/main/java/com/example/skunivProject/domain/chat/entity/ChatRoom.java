package com.example.skunivProject.domain.chat.entity;

import com.example.skunivProject.global.baseentity.BaseMongoEntity;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Document(collection = "chat_rooms")
public class ChatRoom extends BaseMongoEntity {

    private Long teamId; // SQL Team PK 참조, 참여자는 팀 멤버에서 조회

    private String name; // 채팅방 이름 등
}
