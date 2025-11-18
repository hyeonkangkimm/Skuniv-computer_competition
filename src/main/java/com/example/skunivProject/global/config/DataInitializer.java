package com.example.skunivProject.global.config;

import com.example.skunivProject.domain.users.entity.Users;
import com.example.skunivProject.domain.users.enums.Gender;
import com.example.skunivProject.domain.users.enums.Rank;
import com.example.skunivProject.domain.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        String masterUsername = "masteruser";

        // 마스터 유저가 이미 존재하는지 확인
        if (!userRepository.findByUsername(masterUsername).isPresent()) {
            Users masterUser = Users.builder()
                    .username(masterUsername)
                    .password(passwordEncoder.encode("password")) // 비밀번호는 "password" 입니다.
                    .name("마스터유저")
                    .phone("010-0000-0000") // 임의의 전화번호
                    .gender(Gender.MALE)      // 임의의 성별
                    .birth(LocalDate.of(1990, 1, 1)) // 임의의 생년월일
                    .rank(Rank.TREE) // ** 목표 등급인 Tree 설정 **
                    .email("kkhk75@naver.com")
                    .build();

            userRepository.save(masterUser);
            log.info("초기 데이터: Tree 등급의 마스터 유저 생성 완료 (username: {})", masterUsername);

            String seedUsername1 = "seeduser1";

            // 마스터 유저가 이미 존재하는지 확인
            if (!userRepository.findByUsername(seedUsername1).isPresent()) {
                Users seedUser = Users.builder()
                        .username(seedUsername1)
                        .password(passwordEncoder.encode("password")) // 비밀번호는 "password" 입니다.
                        .name("시드유저1")
                        .phone("010-0000-0001") // 임의의 전화번호
                        .gender(Gender.MALE)      // 임의의 성별
                        .birth(LocalDate.of(1990, 1, 1)) // 임의의 생년월일
                        .rank(Rank.SEED) // **seed user 설정 **
                        .email("kkhk75@naver.com1")
                        .build();

                userRepository.save(seedUser);
                log.info("초기 데이터: seed 등급의 seed 유저 생성 완료 (username: {})", seedUsername1);
            }
        }
    }
}
