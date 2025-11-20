package com.example.skunivProject.domain.chat.controller;

import com.example.skunivProject.domain.chat.dto.RequestDto;
import com.example.skunivProject.domain.chat.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatKafkaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * WebSocket → /pub/chat.send 로 메시지가 들어오면 실행
     */
    @MessageMapping("/chat.send")
    public void send(RequestDto.ChatMessageSend req) {

        // Kafka로 보낼 메시지 변환
        ResponseDto.ChatMessage kafkaMessage = ResponseDto.ChatMessage.builder()
                .roomId(req.getRoomId())
                .type("TALK")
                .body(ResponseDto.ChatMessage.MessageBody.builder()
                        .senderId(req.getSenderId())
                        .senderName(req.getSenderName())
                        .message(req.getMessage())
                        .timestamp(System.currentTimeMillis())
                        .build())
                .build();

        // Kafka 전송 (Partition Key = roomId → 같은 방 메시지는 같은 Partition)
        kafkaTemplate.send("chat-topic", req.getRoomId(), kafkaMessage);
    }
}
