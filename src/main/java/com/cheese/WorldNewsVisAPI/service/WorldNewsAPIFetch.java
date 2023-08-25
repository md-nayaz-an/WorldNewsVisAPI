package com.cheese.WorldNewsVisAPI.service;

import com.cheese.WorldNewsVisAPI.models.NewsFetchData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;



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

        return webClient.get()
                .uri(
                        "?text=" + query +
                                "&earliest-publish-date=2023-07-22" +
                                "&latest-publish-date=2023-08-22" +
                                "&number=100" +
                                "&api-key=" + apikey
                )
                .retrieve()
                .bodyToMono(NewsFetchData.class)
                .block();
    }
}
