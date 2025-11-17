package com.example.skunivProject.domain.competition.crawler.config;

import com.example.skunivProject.domain.competition.crawler.service.ContestCrawlerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CrawlerRunner implements CommandLineRunner {

    private final ContestCrawlerService crawlerService;

    public CrawlerRunner(ContestCrawlerService crawlerService) {
        this.crawlerService = crawlerService;
    }

    @Override
    public void run(String... args) {

        // 크롤링할 querystr 목록
        List<String> queryStrings = List.of(
                "rSW3rryZv7-Ml0u2sExn_BIMcFbE8UePii7n-FYvg9lEmDI7rZMRz6timFPCLDnwk8edz8jdAzGh307G_KdFG2w4rIR7I_GsxKytjDIo41XvhjT3hjQVBwABANyoHRNZ",
                "rSW3rryZv7-Ml0u2sExn_P0lEAqfXP0OCfne2mLUsdq8QKK5mC1w5xTankJdb_FEanOHYMI84z8aczST5MdQYIv5YxXysQHWyVspqWxFdMIZZRI93qXI4FGMJTqykVDE",
                "rSW3rryZv7-Ml0u2sExn_I6pF7X_Tdq-NPWJmvghb13B_MYm0sJyWfcHFEhe-K2Xvcn3BHhjeLdYYycfww00CCSpJWDkZV37g-NwqxTK1rGtmcCNS2wO-spOp8l9EwsA",
                "rSW3rryZv7-Ml0u2sExn_GnUFkgGsL56MiV9VLpdgvJu1HAgV6ynA8n75rVZ_YNlIH0qdNMr5BUnNhe4pREmGRV7YuUNCFhND7K-RhpD_gSir1So16nHoiFzEsoYxCqa",
                "rSW3rryZv7-Ml0u2sExn_Ca0gsqmgiwAPe3TcSsWQ9lX5esXav9ASqRCVlHNIANCR2SKAhUUa0MkBZhYus2pLrYuCcbScbtqDHIxqVFKfkFyre3hcwrzhcSyIEwdVZBm",
                "rSW3rryZv7-Ml0u2sExn_OV8fmXCDNZJKOQ1Y23LmI3TSCgy2uiFzM65Tc6ElAzE9qvBTIdVrdqBMoEs1-jIz_eayInvzS79FHLzV27JsRIunaJuiZviD7uIgu3oNNbT",
                "rSW3rryZv7-Ml0u2sExn_FTcagdHthlLwhPtjsedrA_bNreMYi-b6JIE8y7a_WNbKPyhv43Bv89zrWHyf5k3CdcgcOCZfCNS3Io0iCHU56YvO1FKHLaaq2JzmG6ML7l-"

        );
        for (String qs : queryStrings) {
            String contestUrl = "https://www.thinkcontest.com/thinkgood/user/contest/view.do?querystr=" + qs;
            crawlerService.crawlAndSave(contestUrl); // <- 여기서 호출
        }

    }
}