package com.example.skunivProject.domain.users.service;

import com.example.skunivProject.domain.users.dto.RequestDto;
import com.example.skunivProject.domain.users.dto.ResponseDto;
import com.example.skunivProject.domain.users.entity.Users;
import com.example.skunivProject.domain.users.exception.UserException;
import com.example.skunivProject.domain.users.exception.code.UserErrorCode;
import com.example.skunivProject.domain.users.repository.UserRepository;
import com.example.skunivProject.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public void signUp(RequestDto.SignUpDto signUpDto) {
        if (userRepository.findByUsername(signUpDto.getUsername()).isPresent()) {
            throw new UserException(UserErrorCode.DUPLICATE_USER_ID);
        }

        Users users = Users.builder()
                .username(signUpDto.getUsername())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .name(signUpDto.getName())
                .phone(signUpDto.getPhone())
                .gender(signUpDto.getGender())
                .birth(signUpDto.getBirth())
                .email(signUpDto.getEmail())
                .build();
        userRepository.save(users);
    }

    @Transactional
    public ResponseDto.LoginResponseDto login(RequestDto.LoginDto loginDto) {

        //아이디 존재 여부 및 비밀번호 검증
        Users user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new UserException(UserErrorCode.INVALID_PASSWORD);
        }

        //Spring Security 인증 토큰 생성 및 인증
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        //JWT 토큰 생성
        String accessToken = jwtTokenProvider.createToken(authentication);

        //응답 DTO에 추가 정보(name, rank)를 담아 반환
        return ResponseDto.LoginResponseDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .name(user.getName()) // 사용자 이름 추가
                .rank(user.getRank())   // 사용자 등급 추가
                .build();
    }
}
