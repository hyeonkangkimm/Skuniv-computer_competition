package com.example.skunivProject.domain.chat.repository;

import com.example.skunivProject.domain.chat.entity.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    /**
     * 특정 채팅방의 메시지를 페이징하여 조회합니다.
     * @param roomId 채팅방 ID
     * @param pageable 페이징 정보 (예: 최신 50개)
     * @return 메시지 목록
     */
    List<ChatMessage> findByRoomId(String roomId, Pageable pageable);
}
