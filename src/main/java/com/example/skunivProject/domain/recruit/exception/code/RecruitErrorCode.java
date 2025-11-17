package com.example.skunivProject.domain.recruit.exception.code;

import com.example.skunivProject.global.apipayload.code.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum RecruitErrorCode implements BaseErrorCode {

    // 모집공고 생성 관련
    RECRUIT_NOT_ALLOWED(HttpStatus.FORBIDDEN, "RECRUIT403_1", "Recruit 권한이 있는 사용자만 작성 가능합니다."),
    COMPETITION_NOT_FOUND(HttpStatus.NOT_FOUND, "COMP404_1", "해당 공모전을 찾을 수 없습니다."),
    COMPETITION_DELETED(HttpStatus.BAD_REQUEST, "COMP400_1", "삭제된 공모전에는 모집글을 작성할 수 없습니다."),

    // 모집공고 지원 관련
    RECRUIT_POST_NOT_FOUND(HttpStatus.NOT_FOUND, "RECRUIT404_1", "해당 모집공고를 찾을 수 없습니다."),
    ALREADY_APPLIED(HttpStatus.CONFLICT, "APPLY409_1", "이미 지원한 모집공고입니다."),
    CAPACITY_EXCEEDED(HttpStatus.BAD_REQUEST, "APPLY400_1", "모집 정원이 마감되었습니다."),
    CANNOT_APPLY_TO_OWN_POST(HttpStatus.BAD_REQUEST, "APPLY400_2", "자신이 작성한 모집공고에는 지원할 수 없습니다."),

    // 지원자 목록 조회 관련
    FORBIDDEN_TO_VIEW_APPLIES(HttpStatus.FORBIDDEN, "APPLY403_1", "지원자 목록을 조회할 권한이 없습니다."),

    // 지원 상태 변경 관련
    APPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "APPLY404_1", "해당 지원을 찾을 수 없습니다."),
    FORBIDDEN_TO_UPDATE_STATUS(HttpStatus.FORBIDDEN, "APPLY403_2", "지원 상태를 변경할 권한이 없습니다."),
    INVALID_STATUS_UPDATE(HttpStatus.BAD_REQUEST, "APPLY400_3", "APPLIED 상태에서만 상태를 변경할 수 있습니다."),
    CANNOT_UPDATE_TO_APPLIED(HttpStatus.BAD_REQUEST, "APPLY400_4", "APPLIED 상태로는 변경할 수 없습니다."),

    // 최종 팀 정보 조회 관련
    POST_NOT_CLOSED(HttpStatus.BAD_REQUEST, "TEAM400_1", "모집이 마감되지 않아 최종 팀 정보를 조회할 수 없습니다.");


    private final HttpStatus status;
    private final String code;
    private final String message;
}
