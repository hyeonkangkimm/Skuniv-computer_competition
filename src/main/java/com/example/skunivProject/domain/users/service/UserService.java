package com.example.skunivProject.domain.users.service;

import com.example.skunivProject.domain.users.dto.ResponseDto;
import com.example.skunivProject.domain.users.entity.Users;
import com.example.skunivProject.domain.users.exception.UserException;
import com.example.skunivProject.domain.users.exception.code.UserErrorCode;
import com.example.skunivProject.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    /**
     * 내 계정 정보를 조회하는 서비스
     * @param username 현재 로그인한 사용자의 username
     * @return 계정 정보 DTO
     */
    public ResponseDto.MyAccountDto getMyAccount(String username) {
        // 1. username으로 DB에서 사용자 정보 조회
        Users currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        // 2. 조회된 엔티티를 DTO로 변환하여 반환
        return ResponseDto.MyAccountDto.builder()
                .name(currentUser.getName())
                .rank(currentUser.getRank())
                .point(currentUser.getPoint())
                .phone(currentUser.getPhone())
                .email(currentUser.getEmail())
                .build();
    }
}
