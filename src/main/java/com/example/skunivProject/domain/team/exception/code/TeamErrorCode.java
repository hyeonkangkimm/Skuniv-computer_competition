package com.example.skunivProject.domain.team.exception.code;

import com.example.skunivProject.global.apipayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TeamErrorCode implements BaseErrorCode {

    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "TEAM404_1", "해당 팀을 찾을 수 없습니다."),
    FORBIDDEN_TO_FINISH_TEAM(HttpStatus.FORBIDDEN, "TEAM403_1", "팀을 종료할 권한이 없습니다. (팀 리더만 가능)"),
    COMPETITION_NOT_ENDED(HttpStatus.BAD_REQUEST, "TEAM400_1", "아직 공모전이 종료되지 않았습니다."),
    TEAM_ALREADY_FINISHED(HttpStatus.CONFLICT, "TEAM409_1", "이미 활동이 종료된 팀입니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
