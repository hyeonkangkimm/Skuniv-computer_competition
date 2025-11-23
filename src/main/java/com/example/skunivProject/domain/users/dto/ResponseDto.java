package com.example.skunivProject.domain.users.dto;

import com.example.skunivProject.domain.users.enums.Rank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class LoginResponseDto {
        private String grantType;
        private String accessToken;
        private String name;
        private Rank rank;
    }

    /**
     * 계정 보기(내 정보 조회)를 위한 응답 DTO
     */
    @Getter
    @Builder
    public static class MyAccountDto {
        private String name;
        private Rank rank;
        private int point;
        private String phone;
        private String email;
    }
}
