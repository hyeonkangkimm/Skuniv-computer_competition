package com.example.skunivProject.domain.users.dto;

import com.example.skunivProject.domain.users.enums.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class RequestDto {

    @Getter
    @NoArgsConstructor
    public static class SignUpDto {
        private String username;
        private String name;
        private String password;
        private String phone;
        private Gender gender;
        private LocalDate birth;
    }

    @Getter
    @NoArgsConstructor
    public static class LoginDto {
        private String username;
        private String password;
    }
}
