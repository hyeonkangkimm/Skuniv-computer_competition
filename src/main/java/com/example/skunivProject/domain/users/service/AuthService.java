package com.example.skunivProject.domain.users.service;

import com.example.skunivProject.domain.users.entity.Users;
import com.example.skunivProject.domain.users.exception.UserException;
import com.example.skunivProject.domain.users.exception.code.UserErrorCode;
import com.example.skunivProject.domain.users.repository.UserRepository;
import com.example.skunivProject.domain.users.dto.RequestDto;
import com.example.skunivProject.domain.users.dto.ResponseDto;
import com.example.skunivProject.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

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
                .build();
        // rank와 point는 엔티티에 기본값이 설정되어 있으므로 빌더에 포함하지 않음.

        userRepository.save(users);
    }

    @Transactional
    public ResponseDto.TokenDto login(RequestDto.LoginDto loginDto) {

        // 1. 아이디 존재 여부 체크
        Users user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new UserException(UserErrorCode.INVALID_PASSWORD);
        }

        // 3. JWT 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        String accessToken = jwtTokenProvider.createToken(authentication);

        return ResponseDto.TokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .build();
    }

}
