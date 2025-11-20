package com.example.skunivProject.domain.chat.exception.code;

import com.example.skunivProject.global.apipayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ChatErrorCode implements BaseErrorCode {

    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "TEAM404_1", "해당 팀을 찾을 수 없습니다."),
    FORBIDDEN_TO_CREATE_CHATROOM(HttpStatus.FORBIDDEN, "CHAT403_1", "채팅방을 생성할 권한이 없습니다. (팀 리더만 가능)"),
    CHATROOM_ALREADY_EXISTS(HttpStatus.CONFLICT, "CHAT409_1", "해당 팀의 채팅방이 이미 존재합니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
