package com.cheese.WorldNewsVisAPI.service;

import com.cheese.WorldNewsVisAPI.Logger;
import com.cheese.WorldNewsVisAPI.models.CountryNewsModel;
import com.cheese.WorldNewsVisAPI.models.NewsFetchArticle;
import com.cheese.WorldNewsVisAPI.models.NewsFetchData;
import com.cheese.WorldNewsVisAPI.models.RestCountryModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NewsService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JsonResponseFormatter jsonResponseFormatter;

    @Autowired
    WorldNewsAPIFetch worldNewsAPIFetch;
    @Autowired
    CountryInfoFetch countryInfoFetch;


    @Autowired
    Logger logger;

    public String getAllNews() {
        return "test";
    }

    public static final Map<String, RestCountryModel> countryModelMap = new HashMap<>();

    public Flux<String> searchNewsStream(String queryString) {

        NewsFetchData newsFetchData = worldNewsAPIFetch.fetch(queryString.trim().replace(' ', '+'));
        System.out.println("Fetch: " + queryString);

        List<NewsFetchArticle> newsList = newsFetchData.getNews();


        Map<String, List<NewsFetchArticle>> map = newsList.stream()
                .filter(article -> !article.getSource_country().trim().isEmpty())
                .collect(Collectors
                        .groupingBy(NewsFetchArticle::getSource_country));

        map.keySet().forEach(key -> {
            if(key.trim().isEmpty())
                key = "us";
            if (!countryModelMap.containsKey(key)) {
                countryModelMap.put(key, countryInfoFetch.fetch(key));
            }
        });

        return Flux.fromIterable(map.entrySet())
                .map(entry -> {
                    //System.out.println(entry.getValue());
                    CountryNewsModel countryNewsModel = jsonResponseFormatter.format(entry.getValue(), entry.getKey());
                    try {
                        return objectMapper.writeValueAsString(countryNewsModel);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
    }


    public Flux<String> searchNewsTestStream(String queryString) {
        String resourcePath = "sampleData/" + queryString.replace(' ', '_') + ".json";
        System.out.println("Test: " + queryString);
        
        InputStream inputStream = NewsService.class.getClassLoader().getResourceAsStream(resourcePath);

        NewsFetchData newsFetchData = null;
        if (inputStream != null) {
            try {
                // Use ObjectMapper to deserialize the JSON data
                ObjectMapper objectMapper = new ObjectMapper();
                newsFetchData = objectMapper.readValue(inputStream, NewsFetchData.class);

                // Now 'newsFetchData' contains the deserialized JSON data
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else
            return Flux.just("Error");

        assert newsFetchData != null;
        List<NewsFetchArticle> newsList = newsFetchData.getNews();


        Map<String, List<NewsFetchArticle>> map = newsList.stream()
                .filter(article -> !article.getSource_country().trim().isEmpty())
                .collect(Collectors
                        .groupingBy(NewsFetchArticle::getSource_country));

        map.keySet().forEach(key -> {
            if (key.trim().isEmpty())
                key = "us";
            if (!countryModelMap.containsKey(key)) {
                countryModelMap.put(key, countryInfoFetch.fetch(key));
            }
        });

        return Flux.fromIterable(map.entrySet())
                .delayElements(Duration.ofSeconds(1))
                .map(entry -> {
                    //System.out.println(entry.getValue());
                    CountryNewsModel countryNewsModel = jsonResponseFormatter.format(entry.getValue(), entry.getKey());
                    try {
                        return objectMapper.writeValueAsString(countryNewsModel);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}