package com.example.skunivProject.domain.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ResponseDto {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class TokenDto {
        private String grantType;
        private String accessToken;
    }
}
