package com.example.skunivProject.domain.competition.crawler.service;

import com.example.skunivProject.domain.competition.crawler.config.CrawlerConfig;
import com.example.skunivProject.domain.competition.entity.Competition;
import com.example.skunivProject.domain.competition.repository.CompetitionRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContestCrawlerService {

    private static final Logger log = LoggerFactory.getLogger(ContestCrawlerService.class);

    private final CompetitionRepository competitionRepository;
    private final CrawlerConfig config; // BaseUrl, UserAgent, Timeout 등

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ContestCrawlerService(CompetitionRepository competitionRepository, CrawlerConfig config) {
        this.competitionRepository = competitionRepository;
        this.config = config;
    }

    public void crawlAndSave(String contestUrl) {
        log.info("==== 크롤링 시작: {} ====", contestUrl);
        try {
            Document doc = Jsoup.connect(contestUrl)
                    .userAgent(config.getUserAgent())
                    .timeout(config.getTimeout())
                    .get();

            // 1. 제목
            String title = Optional.ofNullable(doc.selectFirst("input#sns_program_nm"))
                    .map(e -> e.attr("value"))
                    .orElse("");

            // 2. 주최
            String host = Optional.ofNullable(doc.selectFirst(".tit:contains(주최) + .txt"))
                    .map(Element::text)
                    .orElse("");

            // 3. 주관
            String organizer = Optional.ofNullable(doc.selectFirst(".tit:contains(주관) + .txt"))
                    .map(Element::text)
                    .orElse("");

            // 4. 응모분야
            String category = doc.select(".tit:contains(응모분야) + .txt li").stream()
                    .map(Element::text).map(String::trim)
                    .collect(Collectors.joining(", "));

            // 5. 참가자격
            String qualification = doc.select(".tit:contains(참가자격) + .txt li").stream()
                    .map(Element::text).map(String::trim)
                    .collect(Collectors.joining(", "));

            // 6. 접수기간
            String period = Optional.ofNullable(doc.selectFirst(".tit:contains(접수기간) + .txt"))
                    .map(Element::text).orElse("");
            LocalDateTime applyStart = null;
            LocalDateTime applyEnd = null;
            if (period.contains("~")) {
                String[] parts = period.split("~");

                String startStr = parts[0].trim();
                String endStr = parts[1].trim();

                // 시간 없으면 기본 "00:00" 추가
                if (startStr.length() == 10) startStr += " 00:00";
                if (endStr.length() == 10) endStr += " 00:00";

                applyStart = LocalDateTime.parse(startStr, dtf);
                applyEnd = LocalDateTime.parse(endStr, dtf);
            }

            // 7. 시상금
            String awardFirst = Optional.ofNullable(doc.selectFirst(".tit:contains(시상금) + .txt li"))
                    .map(Element::text).orElse("");

            // 8. 주최사 홈페이지
            String homePage = Optional.ofNullable(doc.selectFirst(".tit:contains(홈페이지) + .txt a"))
                    .map(e -> e.attr("href")).orElse("");

            // 9. 씽굿 링크 (현재 크롤링 URL)
            String thinkGoodLink = contestUrl;

            // 10. 이미지 URL
            String imgUrl = Optional.ofNullable(doc.selectFirst(".img-wrap a img"))
                    .map(e -> e.attr("src")).orElse("");

            // 엔티티 생성
            Competition competition = Competition.builder()
                    .title(title)
                    .host(host)
                    .organizer(organizer)
                    .category(category)
                    .qualification(qualification)
                    .applyStart(applyStart)
                    .applyEnd(applyEnd)
                    .awardFirst(awardFirst)
                    .homePage(homePage)
                    .thinkGoodLink(thinkGoodLink)
                    .imgUrl(imgUrl)
                    .build();

            // applyEnd가 현재 시간 지났으면 논리 삭제
            if (competition.getApplyEnd() != null && competition.getApplyEnd().isBefore(LocalDateTime.now())) {
                competition.delete(); // isDeleted=true, deletedAt=now
                competitionRepository.save(competition);
                log.info("논리 삭제 처리됨: {}", competition.getTitle());
            } else {
                competitionRepository.save(competition);
                log.info("저장 완료: {}", competition.getTitle());
            }
        } catch (IOException e) {
            log.error("크롤링 실패: {}", e.getMessage());
        } catch (Exception e) {
            log.error("저장 실패: {}", e.getMessage(), e);
        }
    }
}
