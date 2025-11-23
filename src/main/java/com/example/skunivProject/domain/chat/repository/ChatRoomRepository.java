package com.example.skunivProject.domain.chat.repository;

import com.example.skunivProject.domain.chat.entity.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    /**
     * 특정 팀 ID로 생성된 채팅방을 조회
     */
    Optional<ChatRoom> findByTeamId(Long teamId);

    /**
     * 특정 팀 ID로 생성된 채팅방을 삭제
     */
    void deleteByTeamId(Long teamId);
}
