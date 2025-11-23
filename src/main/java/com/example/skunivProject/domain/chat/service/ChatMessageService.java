package com.example.skunivProject.domain.chat.service;

import com.example.skunivProject.domain.chat.entity.ChatMessage;
import com.example.skunivProject.domain.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository messageRepository;
    // ChatRoomService, UserRepository 의존성 제거

    /**
     * 채팅 메시지를 MongoDB에 저장
     */
    @Transactional
    public ChatMessage saveMessage(String roomId, Long userId, String message) {
        ChatMessage newMessage = ChatMessage.builder()
                .roomId(roomId)
                .userId(userId)
                .message(message)
                .build();
        return messageRepository.save(newMessage);
    }

    /**
     * 특정 채팅방의 이전 대화 내역을 페이징하여 조회
     */
    @Transactional(readOnly = true)
    public List<ChatMessage> getChatMessages(String roomId, int page, int size) {
        // 권한 체크 로직을 완전히 제거합니다.
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return messageRepository.findByRoomId(roomId, pageable);
    }
}
