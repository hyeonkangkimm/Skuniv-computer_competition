package com.example.skunivProject.domain.recruit.exception.code;

import com.example.skunivProject.global.apipayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum RecruitErrorCode implements BaseErrorCode {

    RECRUIT_NOT_ALLOWED(HttpStatus.FORBIDDEN, "RECRUIT403_1", "Recruit 권한이 있는 사용자만 작성 가능합니다."),
    COMPETITION_NOT_FOUND(HttpStatus.NOT_FOUND, "COMP404_1", "해당 공모전을 찾을 수 없습니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;
}
