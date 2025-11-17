package com.example.skunivProject.domain.users.exception.code;

import com.example.skunivProject.global.apipayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements BaseErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"USER404_1","유저를 찾을 수 없습니다."),
    DUPLICATE_USER_ID(HttpStatus.CONFLICT, "USER409_1", "이미 사용중인 아이디입니다."),
    INVALID_USER_ID(HttpStatus.BAD_REQUEST, "USER400_1", "잘못된 아이디입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "USER401_1", "비밀번호가 일치하지 않습니다."),
    USER_LOGIN_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "USER500_1", "로그인 처리 중 오류가 발생했습니다.");
    private final HttpStatus status;
    private final String code;
    private final String message;
}
