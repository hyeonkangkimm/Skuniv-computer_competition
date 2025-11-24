package com.example.skunivProject.global.config;

import com.example.skunivProject.domain.competition.entity.Competition;
import com.example.skunivProject.domain.competition.repository.CompetitionRepository;
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
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompetitionRepository competitionRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        String masterUsername = "masteruser";
        if (!userRepository.findByUsername(masterUsername).isPresent()) {
            Users masterUser = Users.builder()
                    .username(masterUsername)
                    .password(passwordEncoder.encode("password"))
                    .name("마스터유저")
                    .phone("010-0000-0000")
                    .gender(Gender.MALE)
                    .birth(LocalDate.of(1990, 1, 1))
                    .rank(Rank.TREE)
                    .email("kkhk75@naver.com")
                    .build();
            userRepository.save(masterUser);
            log.info("초기 데이터: Tree 등급의 마스터 유저 생성 완료 (username: {})", masterUsername);
        }

        String seedUsername1 = "seeduser1";
        if (!userRepository.findByUsername(seedUsername1).isPresent()) {
            Users seedUser = Users.builder()
                    .username(seedUsername1)
                    .password(passwordEncoder.encode("password"))
                    .name("시드유저1")
                    .phone("010-0000-0001")
                    .gender(Gender.MALE)
                    .birth(LocalDate.of(1990, 1, 1))
                    .rank(Rank.SEED)
                    .email("kkhk75@naver.com1")
                    .build();
            userRepository.save(seedUser);
            log.info("초기 데이터: SEED 등급의 유저 생성 완료 (username: {})", seedUsername1);
        }

        String seedUsername2 = "seeduser2";
        if (!userRepository.findByUsername(seedUsername2).isPresent()) {
            Users seedUser = Users.builder()
                    .username(seedUsername2)
                    .password(passwordEncoder.encode("password"))
                    .name("시드유저2")
                    .phone("010-0000-0002")
                    .gender(Gender.FEMALE)
                    .birth(LocalDate.of(1992, 2, 2))
                    .rank(Rank.SEED)
                    .email("kkhk75@naver.com2")
                    .build();
            userRepository.save(seedUser);
            log.info("초기 데이터: SEED 등급의 유저 생성 완료 (username: {})", seedUsername2);
        }

        String seedUsername3 = "seeduser3";
        if (!userRepository.findByUsername(seedUsername3).isPresent()) {
            Users seedUser = Users.builder()
                    .username(seedUsername3)
                    .password(passwordEncoder.encode("password"))
                    .name("시드유저3")
                    .phone("010-0000-0003")
                    .gender(Gender.MALE)
                    .birth(LocalDate.of(1993, 3, 3))
                    .rank(Rank.SEED)
                    .email("kkhk75@naver.com3")
                    .build();
            userRepository.save(seedUser);
            log.info("초기 데이터: SEED 등급의 유저 생성 완료 (username: {})", seedUsername3);
        }

        String seedUsername4 = "seeduser4";
        if (!userRepository.findByUsername(seedUsername4).isPresent()) {
            Users seedUser = Users.builder()
                    .username(seedUsername4)
                    .password(passwordEncoder.encode("password"))
                    .name("시드유저4")
                    .phone("010-0000-0004")
                    .gender(Gender.FEMALE)
                    .birth(LocalDate.of(1994, 4, 4))
                    .rank(Rank.SEED)
                    .email("kkhk75@naver.com4")
                    .build();
            userRepository.save(seedUser);
            log.info("초기 데이터: SEED 등급의 유저 생성 완료 (username: {})", seedUsername4);
        }

        String finishedContestTitle = "작년에 끝난 AI 공모전";
        if (!competitionRepository.existsByTitle(finishedContestTitle)) {
            Competition finishedCompetition = Competition.builder()
                    .title(finishedContestTitle)
                    .host("과기부")
                    .organizer("K-AI")
                    .category("인공지능, 딥러닝")
                    .qualification("전국민")
                    .applyStart(LocalDateTime.now().minusYears(1).minusMonths(1))
                    .applyEnd(LocalDateTime.now().minusYears(1))
                    .awardFirst("1000만원")
                    .homePage("http://example.com")
                    .thinkGoodLink("http://example.com/finished")
                    .imgUrl("https://www.thinkcontest.com/thinkgood/common/display.do?filepath=contest_poster/image/&filename=ede23a4443af4c829d4e96bbb148cdeb.jpg&filegubun=poster")
                    .build();
            competitionRepository.save(finishedCompetition);
            log.info("초기 데이터: 종료된 공모전 생성 완료 (title: {})", finishedContestTitle);
        }
    }
}
