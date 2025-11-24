package com.example.skunivProject.domain.chat.kafka;

import com.example.skunivProject.domain.chat.dto.RequestDto;
import com.example.skunivProject.domain.chat.entity.ChatMessage;
import com.example.skunivProject.domain.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatKafkaListener {

    private final ChatBroadcaster broadcaster;
    private final ChatMessageService chatMessageService;

    @KafkaListener(topics = "chat-topic", groupId = "chat-group")
    public void consume(RequestDto.ChatMessageDto chatMessageDTO) {

        if (chatMessageDTO == null) {
            log.warn("Received null chat message from Kafka, ignoring");
            return;
        }

        log.info("Kafka message received: room={}, user={}, username={}, name={}, message={}",
                chatMessageDTO.getRoomId(), chatMessageDTO.getUserId(), chatMessageDTO.getUsername(), chatMessageDTO.getName(), chatMessageDTO.getMessage());

        //Service를 통해 메시지 저장
        ChatMessage savedMessage = chatMessageService.saveMessage(
                chatMessageDTO.getRoomId(),
                chatMessageDTO.getUserId(),
                chatMessageDTO.getMessage()
        );

        // 브로드캐스트하기 전에, 임시 필드에 username과 name 설정
        savedMessage.setUsername(chatMessageDTO.getUsername());
        savedMessage.setName(chatMessageDTO.getName());

        // WebSocket 브로드캐스트
        broadcaster.sendToRoom(savedMessage.getRoomId(), savedMessage);
    }
}
