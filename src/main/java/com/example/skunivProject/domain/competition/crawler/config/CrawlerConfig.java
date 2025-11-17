package com.example.skunivProject.domain.competition.crawler.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CrawlerConfig {

    @Value("${crawler.base-url}")
    private String baseUrl;

    @Value("${crawler.timeout}")
    private int timeout;

    @Value("${crawler.user-agent}")
    private String userAgent;

    public String getBaseUrl() { return baseUrl; }
    public int getTimeout() { return timeout; }
    public String getUserAgent() { return userAgent; }
}
