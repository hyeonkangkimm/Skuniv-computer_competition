package com.example.skunivProject.domain.chat.kafka;

import com.example.skunivProject.domain.chat.dto.ResponseDto;
import com.example.skunivProject.domain.chat.entity.ChatMessage;
import com.example.skunivProject.domain.chat.service.ChatMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatKafkaListener {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "chat-topic", groupId = "chat-group")
    public void consume(Object messageObj) {

        // Kafka 에서 온 메시지를 ResponseDto.ChatMessage 로 변환
        ResponseDto.ChatMessage kafkaMsg =
                objectMapper.convertValue(messageObj, ResponseDto.ChatMessage.class);

        // ===== 1) MongoDB 저장 =====
        ChatMessage saved = chatMessageService.saveMessage(
                kafkaMsg.getRoomId(),
                kafkaMsg.getBody().getSenderId(),
                kafkaMsg.getBody().getMessage()
        );

        // ===== 2) WebSocket 브로드캐스트 =====
        messagingTemplate.convertAndSend(
                "/sub/chat/room/" + kafkaMsg.getRoomId(),
                saved   // 저장된 메시지 그대로 브로드캐스트
        );
    }
}
