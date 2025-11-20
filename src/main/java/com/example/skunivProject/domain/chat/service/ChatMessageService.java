package com.example.skunivProject.domain.chat.service;

import com.example.skunivProject.domain.chat.entity.ChatMessage;
import com.example.skunivProject.domain.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository messageRepository;

    @Transactional
    public ChatMessage saveMessage(String roomId, Long userId, String message) {

        // 1. 새로운 ChatMessage 문서를 생성합니다.
        ChatMessage newMessage = ChatMessage.builder()
                .roomId(roomId)
                .userId(userId)
                .message(message)
                .build();

        // 2. MongoDB에 메시지를 저장하고, 저장된 문서를 반환합니다.
        return messageRepository.save(newMessage);
    }
}
