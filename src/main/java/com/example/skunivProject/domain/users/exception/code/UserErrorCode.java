package com.example.skunivProject.domain.users.exception.code;

import com.example.skunivProject.global.apipayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCode implements BaseErrorCode {

    // 일반적인 에러
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER404_1", "해당 유저를 찾을 수 없습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "USER400_1", "비밀번호가 일치하지 않습니다."),

    // 회원가입 관련
    DUPLICATE_USER_ID(HttpStatus.CONFLICT, "USER409_1", "이미 존재하는 아이디입니다."),

    // 로그인/인증 관련
    USER_LOGIN_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "USER500_1", "로그인 처리 중 오류가 발생했습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH401_1", "유효하지 않은 토큰입니다. 다시 로그인해주세요.");


    private final HttpStatus status;
    private final String code;
    private final String message;
}
