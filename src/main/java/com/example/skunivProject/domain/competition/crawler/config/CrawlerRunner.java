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
                "rSW3rryZv7-Ml0u2sExn_FTcagdHthlLwhPtjsedrA_bNreMYi-b6JIE8y7a_WNbKPyhv43Bv89zrWHyf5k3CdcgcOCZfCNS3Io0iCHU56YvO1FKHLaaq2JzmG6ML7l-",
                "rSW3rryZv7-Ml0u2sExn_CG34PX7ku19gB170swAiAYOoq6N_QpsHVbLMCAFBUS3w3L5bXeZ-9bZYSXBKLLthOPra9ngSqSJiihO7wyr8_w",
                "rSW3rryZv7-Ml0u2sExn_Ca0gsqmgiwAPe3TcSsWQ9lX5esXav9ASqRCVlHNIANCR2SKAhUUa0MkBZhYus2pLrYuCcbScbtqDHIxqVFKfkFyre3hcwrzhcSyIEwdVZBm",
                "rSW3rryZv7-Ml0u2sExn_HOCwH6jMlVvpRR7pykuybv8ocsbWYzZpU7ccZ5BT6wrLLgf7u5GCv3YPacy2B2lodD-qY8eMi0epnyYkyNc3Ri_dFIF7nz0Ij6-fXEcAsoh",
                "rSW3rryZv7-Ml0u2sExn_Ig9pt-Rb3nxi2icDmGXUpQuWoAWzCFh9yOXLEzzngo171-8y4Q5uMOEWCtlTzeVbitTWVVGSXnsv0qDafd-IVhnjkqBuOM5J3s1bdpfEJ8f",
                "rSW3rryZv7-Ml0u2sExn_NFybJsG7YB4v26hiWpawftBpx0Ow-IUOOSs_Y1GAgQKZOtBG74tCu6U_4IddV2MOn2DQ8AHY0uMVGNc94nhI25mkIMMDl5KuebcJyufrnY_",
                "rSW3rryZv7-Ml0u2sExn_JPQvfG6X2jRxjxd5yB1LyRdN1e2JDtNt1pfELP8OS03_MxEaejeLShajbGkIy7lmaO5ObqNG0JlfTGsNzoUBERlxIFaT_GWxXNPkCtha9S0",
                "jSz0J0WNJB45MZv9sO3VweY8z1ulOyoqGVdV1auFpzX-BaeqqfhQSA1H_Mu3KRdvdDC3pRXoPVTS_795YXuginU2dErqWfVui_H9gZW6czNg5qAJkb3bWIMVgga0OB_j",
                "rSW3rryZv7-Ml0u2sExn_BTMxbpVNsbGeTlCHnWI-eCZScnOUkc9PbmCRX0JWzbrKeOw_VGuWE5EdaXm9VkInE-K2wagyLAVHLWLtfm_GtV1I40GJx9kXbzB3vZ9KNNA",
                "rSW3rryZv7-Ml0u2sExn_EjRUn7-loXCYPYxsuM1vNyvjc9iPXHVcM4h-g6LTY5tW6tnMceaiGpHwqr6kiWWkBE21VBtcvAqwg2x-4umUyvLKoR8Hdp6xaOi6aVDMRj0",
                "rSW3rryZv7-Ml0u2sExn_B0tj88HIhKiuM43g1BIeHiC3a3ldu5ziMV-VC-0eVBjIngZiNg12cl3oVwOMowoI-FZvLFwSPlD3G5N69NWOb1T3-0Cu7BfqkugTJ_VIe0W",
                "rSW3rryZv7-Ml0u2sExn_MylO_vKyzUn5lp3RgF7BzwjzZj92FficWRa_pd6kGOonu67_d6XDAgGyDB9p9kfPC-lEKRyDddnmgciHpnt0KkD0MvyRQpujHEVrD91znGe",
                "rSW3rryZv7-Ml0u2sExn_N37l5nbg3qu1f3BgLcnBnAvK9ojLTDgztVRaFGE8j-twCNaMqUTUHe7PzmK1oTtlOJH_t8aC7u4I5g8AXnWg6ApDhV39BhUwMlN22NHJGFC",
                "rSW3rryZv7-Ml0u2sExn_NSLhvN8gxVJw_VteTMxRD6li629B5nE2dBmwWj28ZUJpKvKNIpuoJCI22JFc0eDtApYAA7oDv3mECmEzR_5zkBqfJw-wqM5ribDLIo4offa",
                "rSW3rryZv7-Ml0u2sExn_M3lrT3Y8YUBTctxbElCD95JVeP4WQP_B4b17gDls8RwGM6DchgQcDSm_v6ggqwkfnwKa339WgflT3PvBWNPMvDidzrmELBheHpjlSOXA0CE",
                "rSW3rryZv7-Ml0u2sExn_PlDknf7x3W7mIQ_BV7YD3YlEDmBPwRF9Us2lxttAYdI9GyuBXMRG49LGBPdwJ-DFq0Pw60reliriEx-I4Jnz3La2arMo2yJc0GHb3B7hADH",
                "rSW3rryZv7-Ml0u2sExn_CvmcNQBf8_L6F-_iAzbK-nOmJbRIv9WcETHYVrmgXDg4J9-6BNLuy0nL6sraHuIa9aypa_oPkrIhON5b_l4w3ErDUScR1jGQHqoX-ptWPbO",
                "rSW3rryZv7-Ml0u2sExn_KiO9k79CvXfe7SNJnQeQA8ZLxuo4jLYdBe1dv0MIMt0IfGgwHBEHfdD5lzUd-j6mPNi5o4uv65hhHFRx56PtOKUP7pNrPkKBTODcjkGi8_6",
                "rSW3rryZv7-Ml0u2sExn_B0tj88HIhKiuM43g1BIeHj3KsCVL6zSz0794X7s7-zlnB3qsNKO2cg0BXq1rTwGr_v_zNSCnh7VBmyT78l8aDdsFIZ0L4BLJ7u6hNZMse57",
                "rSW3rryZv7-Ml0u2sExn_KLIO-H_nDopEx9Acg8sgPMi1cZgaCp67VKTdOR5tNix7rgLC5aMfQ4nQJQsn7eYgSTjUOFLv_9UehGZ4-nn0SvN1GLxtgP-SuIx0mRCFpJF"


        );
        for (String qs : queryStrings) {
            String contestUrl = "https://www.thinkcontest.com/thinkgood/user/contest/view.do?querystr=" + qs;
            crawlerService.crawlAndSave(contestUrl); // <- 여기서 호출
        }

    }
}