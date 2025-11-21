package com.example.skunivProject.global.config.websocket;

import com.example.skunivProject.domain.chat.dto.RequestDto;
import com.example.skunivProject.domain.chat.kafka.KafkaChatProducer;
import com.example.skunivProject.domain.chat.service.ChatSessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;


@Component
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ChatSessionManager sessionManager;
    private final KafkaChatProducer kafkaProducer;
    private final ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessionManager.register(session);
        System.out.println("[+] WebSocket Connected: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        // JSON 파싱
        RequestDto.ChatSocketRequest req = objectMapper.readValue(
                message.getPayload(),
                RequestDto.ChatSocketRequest.class
        );

        switch (req.getType()) {

            case "ENTER" -> {
                sessionManager.enterRoom(req.getRoomId(), session);
                System.out.println("[+] ENTER ROOM :: " + req.getRoomId());
            }

            case "MESSAGE" -> {
                // ChatMessageDto 객체 생성 (Long userId 그대로 사용)
                RequestDto.ChatMessageDto kafkaMsg = RequestDto.ChatMessageDto.builder()
                        .roomId(req.getRoomId())
                        .userId(req.getUserId())  // Long 타입
                        .message(req.getMessage())
                        .timestamp(System.currentTimeMillis())
                        .build();
                // Kafka로 보내기 전에 로그
                log.info("[WebSocket] Sending to Kafka: room={}, user={}, message={}",
                        kafkaMsg.getRoomId(), kafkaMsg.getUserId(), kafkaMsg.getMessage());

                kafkaProducer.sendChatMessage(kafkaMsg);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionManager.deregister(session);
        System.out.println("[+] WebSocket Closed: " + session.getId());
    }
}
