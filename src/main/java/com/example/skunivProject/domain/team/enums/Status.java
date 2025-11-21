package com.example.skunivProject.domain.team.enums;

public enum Status {
    FORMING,    // 구성 중
    COMPLETE,   // 구성 완료 (활동 중)
    FINISHED,   // 정상 활동 종료 (포인트 지급)
    ABANDONED;  // 중도 활동 종료 (포인트 미지급)
}
