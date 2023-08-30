package com.cheese.WorldNewsVisAPI.service;

import com.cheese.WorldNewsVisAPI.models.NewsFetchData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Service
public class WorldNewsAPIFetch {

    @Value("${api.worldnews.key}")
    private String apikey;

    public NewsFetchData fetch(String query) {
        WebClient webClient = WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(codes ->
                                codes.defaultCodecs().maxInMemorySize(
                                        15 * 1024 * 1024
                                )
                        ).build())
                .baseUrl("https://api.worldnewsapi.com/search-news")
                .build();

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(7);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String earliestPublishDate = startDate.format(dateFormatter);
        String latestPublishDate = endDate.format(dateFormatter);

        System.out.println(earliestPublishDate);
        System.out.println(latestPublishDate);
        return webClient.get()
                .uri(
                        "?text=" + query +
                                "&earliest-publish-date=" + earliestPublishDate +
                                "&latest-publish-date=" + latestPublishDate +
                                "&sort=publish-time" +
                                "&sort-direction=DESC" +
                                "&number=100" +
                                "&api-key=" + apikey
                )
                .retrieve()
                .bodyToMono(NewsFetchData.class)
                .block();
    }
}
