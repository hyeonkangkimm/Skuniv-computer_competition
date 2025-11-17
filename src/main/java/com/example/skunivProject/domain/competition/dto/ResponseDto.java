package com.example.skunivProject.domain.competition.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ResponseDto {

    @Getter
    public static class HomeResponseDto {
        private final String title;
        private final String host;
        private final String organizer;
        private final String category;
        private final String qualification;
        private final LocalDateTime applyStart;
        private final LocalDateTime applyEnd;
        private final String awardFirst;
        private final String homePage;
        private final String thinkGoodLink;
        private final String imgUrl;
        private final Long id;

        @Builder
        public HomeResponseDto(String title, String host, String organizer, String category,
                               String qualification, LocalDateTime applyStart, LocalDateTime applyEnd,
                               String awardFirst, String homePage, String thinkGoodLink, String imgUrl,
                               Long id) {
            this.title = title;
            this.host = host;
            this.organizer = organizer;
            this.category = category;
            this.qualification = qualification;
            this.applyStart = applyStart;
            this.applyEnd = applyEnd;
            this.awardFirst = awardFirst;
            this.homePage = homePage;
            this.thinkGoodLink = thinkGoodLink;
            this.imgUrl = imgUrl;
            this.id = id;
        }


    }
}
