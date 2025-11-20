package com.example.skunivProject.global.config.websocket;

import com.example.skunivProject.domain.chat.service.ChatRoomService;
import com.example.skunivProject.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final ChatRoomService chatRoomService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        StompCommand command = accessor.getCommand();

        log.info("STOMP Command: {}, Headers: {}", command, message.getHeaders());

        try {
            if (StompCommand.CONNECT.equals(command)) {
                // 1. CONNECT: JWT 토큰 인증
                if (!handleConnect(accessor)) {
                    log.warn("WebSocket connection refused due to authentication failure.");
                    return null; // 인증 실패 시 연결 거부
                }
            } else if (StompCommand.SUBSCRIBE.equals(command)) {
                // 2. SUBSCRIBE: 채팅방 구독 권한 인가
                handleSubscribe(accessor);
            }
        } catch (Exception e) {
            log.error("STOMP Error: {}", e.getMessage());
            // 구독 실패 등 다른 예외는 명시적으로 예외를 던져 클라이언트에게 전파
            throw new SecurityException(e.getMessage(), e);
        }

        return message;
    }

    /**
     * WebSocket 연결 요청(CONNECT) 시, STOMP 헤더의 JWT 토큰을 검증합니다.
     * @return 인증 성공 시 true, 실패 시 false
     */
    private boolean handleConnect(StompHeaderAccessor accessor) {
        // STOMP 헤더에서 'Authorization' 토큰을 가져옵니다.
        String token = jwtTokenProvider.resolveToken(accessor);
        log.info("Attempting to connect with token: {}", token);

        try {
            if (token == null) {
                log.error("Connection refused: Authorization header is missing in STOMP CONNECT frame.");
                return false; // 토큰이 없으면 연결 거부
            }

            // 토큰 유효성 검사
            jwtTokenProvider.validateToken(token);
            // 유효한 토큰으로부터 Authentication 객체를 생성하여 STOMP 세션에 저장
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            accessor.setUser(authentication);
            log.info("STOMP user set successfully: {}", authentication.getName());
            return true; // 인증 성공

        } catch (Exception e) {
            log.error("Connection refused: Invalid token. Reason: {}", e.getMessage());
            return false; // 토큰 유효성 검사 실패 시 연결 거부
        }
    }

    /**
     * 채팅방 구독 요청(SUBSCRIBE) 시, 해당 사용자가 팀 멤버인지 검사합니다.
     */
    private void handleSubscribe(StompHeaderAccessor accessor) {
        String destination = accessor.getDestination();
        if (destination == null || !destination.startsWith("/sub/chat/room/")) {
            return; // 채팅방 구독이 아니면 검사하지 않음
        }

        // 연결 시 저장했던 사용자 정보(Principal)를 가져옴
        Authentication userAuth = (Authentication) accessor.getUser();
        if (userAuth == null) {
            log.error("Subscription denied: User is not authenticated.");
            throw new SecurityException("Subscription denied: User is not authenticated.");
        }

        String roomId = destination.substring("/sub/chat/room/".length());
        String username = userAuth.getName();

        // 사용자가 해당 채팅방의 멤버인지 검사
        if (!chatRoomService.isUserMemberOfChatRoom(username, roomId)) {
            log.error("Subscription denied for user {} to room {}: Not a member.", username, roomId);
            throw new SecurityException("No permission to subscribe to this chat room.");
        }
        log.info("User {} successfully subscribed to room {}", username, roomId);
    }
}
