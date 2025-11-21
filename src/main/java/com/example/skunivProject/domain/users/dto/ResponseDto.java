package com.example.skunivProject.domain.users.dto;

import com.example.skunivProject.domain.users.enums.Rank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class LoginResponseDto { // TokenDto -> LoginResponseDto로 이름 변경
        private String grantType;
        private String accessToken;
        private String name; // 사용자 이름
        private Rank rank;   // 사용자 등급
    }
}
