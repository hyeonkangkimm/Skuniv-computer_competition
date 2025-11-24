package com.example.skunivProject.global.jwt;

import com.example.skunivProject.domain.users.exception.code.UserErrorCode;
import com.example.skunivProject.global.apipayload.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        UserErrorCode errorCode = UserErrorCode.INVALID_TOKEN;

        response.setStatus(errorCode.getStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        // onFailure 메소드 시그니처에 맞게 호출
        // 첫 번째 인자로 ErrorCode Enum 자체를, 두 번째 인자로 result에 들어갈 값을 전달
        ApiResponse<Object> errorResponse = ApiResponse.onFailure(errorCode, null);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
