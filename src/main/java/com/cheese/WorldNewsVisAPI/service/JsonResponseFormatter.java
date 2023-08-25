package com.cheese.WorldNewsVisAPI.service;

import com.cheese.WorldNewsVisAPI.Logger;
import com.cheese.WorldNewsVisAPI.models.CountryNewsModel;
import com.cheese.WorldNewsVisAPI.models.NewsFetchArticle;
import com.cheese.WorldNewsVisAPI.models.RestCountryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JsonResponseFormatter {

    @Autowired
    CountryInfoFetch countryInfoFetch;

    @Autowired
    Logger logger;


    public List<CountryNewsModel> format(List<NewsFetchArticle> newsList) {

        Map<String, RestCountryModel> countryModelMap = new HashMap<>();

        List<CountryNewsModel> summaries = newsList.stream()
                .collect(Collectors.groupingBy(
                        NewsFetchArticle::getSource_country,
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                articles -> {
                                    String cca2 = articles.get(0).getSource_country();

                                    if(cca2.trim().isEmpty()) {
                                        cca2 = "us";
                                    }

                                    CountryNewsModel summary = new CountryNewsModel();
                                    summary.setCca2(cca2);
                                    summary.setArticles(articles);
                                    summary.setArticleCount(articles.size());
                                    double averageSentiment = articles.stream()
                                            .mapToDouble(NewsFetchArticle::getSentiment)
                                            .average()
                                            .orElse(0.0);
                                    summary.setAverageSentiment(averageSentiment);

                                    if (!countryModelMap.containsKey(cca2)) {
                                        countryModelMap.put(cca2, countryInfoFetch.fetch(cca2));
                                    }

                                    RestCountryModel restCountryModel = countryModelMap.get(cca2);
                                    summary.setCountry(restCountryModel.getCommon());
                                    summary.setLatlng(restCountryModel.getLatlng());

                                    return summary;

                                }
                        )
                ))
                .values()
                .stream()
                .toList();

        return summaries;
    }
}
