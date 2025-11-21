package com.example.skunivProject.domain.chat.kafka;

import com.example.skunivProject.domain.chat.entity.ChatMessage;
import com.example.skunivProject.domain.chat.service.ChatSessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatBroadcaster {

    private final ChatSessionManager sessionManager;
    private final ObjectMapper objectMapper;

    // 방 전체에 메시지 push
    public void sendToRoom(String roomId, ChatMessage message) {
        Set<WebSocketSession> sessions = sessionManager.getSessionsByRoom(roomId);

        if (sessions.isEmpty()) {
            log.warn("No active sessions in room {}. Message ignored.", roomId);
            return;
        }

        try {
            TextMessage textMessage = new TextMessage(
                    objectMapper.writeValueAsString(message)
            );

            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    synchronized (session) { // thread-safe push
                        session.sendMessage(textMessage);
                    }
                }
            }

            log.info("Broadcasted message to {} users in room {}", sessions.size(), roomId);

        } catch (IOException e) {
            log.error("Failed to broadcast message to room {}.", roomId, e);
        }
    }
}
