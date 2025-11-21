package com.example.skunivProject.domain.users.controller;

import com.example.skunivProject.domain.users.dto.RequestDto;
import com.example.skunivProject.domain.users.dto.ResponseDto;
import com.example.skunivProject.domain.users.service.AuthService;
import com.example.skunivProject.global.apipayload.ApiResponse;
import com.example.skunivProject.global.apipayload.code.GeneralSuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signUp(@RequestBody RequestDto.SignUpDto signUpDto) {
        authService.signUp(signUpDto);
        GeneralSuccessCode code = GeneralSuccessCode.USER_CREATED;
        return ResponseEntity.status(code.getStatus())
                .body(ApiResponse.onSuccess(code, null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<ResponseDto.LoginResponseDto>> login(@RequestBody RequestDto.LoginDto loginDto) {
        ResponseDto.LoginResponseDto loginResponseDto = authService.login(loginDto);
        GeneralSuccessCode code = GeneralSuccessCode.SUCCESS;
        return ResponseEntity.status(code.getStatus()).body(ApiResponse.onSuccess(code, loginResponseDto));
    }
}
