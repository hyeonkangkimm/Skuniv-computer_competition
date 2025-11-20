package com.example.skunivProject.global.jwt;

import com.example.skunivProject.global.apipayload.ApiResponse;
import com.example.skunivProject.global.apipayload.code.BaseErrorCode;
import com.example.skunivProject.global.apipayload.code.JwtErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        // WebSocket 연결 요청은 이 필터에서 처리하지 않고 바로 다음으로 넘김
        if (httpServletRequest.getRequestURI().startsWith("/ws")) {
            chain.doFilter(request, response);
            return;
        }

        String token = resolveToken(httpServletRequest);

        try {
            if (StringUtils.hasText(token)) {
                jwtTokenProvider.validateToken(token);
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            setErrorResponse(response, JwtErrorCode.INVALID_TOKEN);
            return;
        } catch (ExpiredJwtException e) {
            setErrorResponse(response, JwtErrorCode.EXPIRED_TOKEN);
            return;
        } catch (UnsupportedJwtException e) {
            setErrorResponse(response, JwtErrorCode.UNSUPPORTED_TOKEN);
            return;
        } catch (IllegalArgumentException e) {
            setErrorResponse(response, JwtErrorCode.INVALID_TOKEN);
            return;
        }

        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private void setErrorResponse(ServletResponse response, BaseErrorCode errorCode) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(errorCode.getStatus().value());
        httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpResponse.setCharacterEncoding("UTF-8");

        ApiResponse<Void> apiResponse = ApiResponse.onFailure(errorCode, null);
        httpResponse.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
