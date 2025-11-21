package com.example.skunivProject.domain.chat.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class ChatSessionManager {

    // 전체 세션 (sessionId → session)
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    // roomId → WebSocketSession Set
    private final Map<String, Set<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();

    // 세션 등록
    public void register(WebSocketSession session) {
        sessions.put(session.getId(), session);
        log.info("New session registered: {}", session.getId());
    }

    // 세션 제거
    public void deregister(WebSocketSession session) {
        sessions.remove(session.getId());

        roomSessions.values().forEach(set -> set.remove(session));
        log.info("Session deregistered: {}", session.getId());
    }

    // 방 입장
    public void enterRoom(String roomId, WebSocketSession session) {
        roomSessions
                .computeIfAbsent(roomId, key -> ConcurrentHashMap.newKeySet())
                .add(session);

        log.info("Session {} entered room {}", session.getId(), roomId);
    }

    // 방에 속한 세션들 가져오기
    public Set<WebSocketSession> getSessionsByRoom(String roomId) {
        return roomSessions.getOrDefault(roomId, Set.of());
    }
}
