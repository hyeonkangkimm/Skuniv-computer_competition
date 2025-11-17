package com.example.skunivProject.global.apipayload.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum JwtErrorCode implements BaseErrorCode {

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "JWT001", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "JWT002", "만료된 토큰입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "JWT003", "지원하지 않는 형식의 토큰입니다."),
    EMPTY_TOKEN(HttpStatus.UNAUTHORIZED, "JWT004", "토큰이 존재하지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
