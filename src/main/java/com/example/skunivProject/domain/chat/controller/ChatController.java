package com.example.skunivProject.domain.chat.controller;

import com.example.skunivProject.domain.chat.dto.ResponseDto;
import com.example.skunivProject.domain.chat.entity.ChatRoom;
import com.example.skunivProject.domain.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomService chatRoomService;

    /**
     * 팀 채팅방 생성
     */
    @PostMapping("/teams/{teamId}/chat-room")
    public ResponseEntity<ResponseDto.ChatRoomCreation> createChatRoom(
            @PathVariable Long teamId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // 서비스를 호출하여 채팅방 생성 로직 수행
        ChatRoom newChatRoom = chatRoomService.createChatRoom(teamId, userDetails.getUsername());

        // 응답 DTO 생성
        ResponseDto.ChatRoomCreation response = ResponseDto.ChatRoomCreation.builder()
                .roomId(newChatRoom.getId())
                .roomName(newChatRoom.getName())
                .teamId(newChatRoom.getTeamId())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 내가 속한 채팅방 목록 조회
     */
    @GetMapping("/my/chat-rooms")
    public ResponseEntity<List<ResponseDto.ChatRoomInfo>> getMyChatRooms(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        List<ResponseDto.ChatRoomInfo> response = chatRoomService.getMyChatRooms(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }
}
