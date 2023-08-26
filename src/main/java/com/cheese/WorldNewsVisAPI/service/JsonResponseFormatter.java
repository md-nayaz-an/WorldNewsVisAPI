package com.cheese.WorldNewsVisAPI.service;

import com.cheese.WorldNewsVisAPI.Logger;
import com.cheese.WorldNewsVisAPI.models.CountryNewsModel;
import com.cheese.WorldNewsVisAPI.models.NewsFetchArticle;
import com.cheese.WorldNewsVisAPI.models.RestCountryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.cheese.WorldNewsVisAPI.service.NewsService.countryModelMap;

@Service
public class JsonResponseFormatter {

    @Autowired
    Logger logger;

    public CountryNewsModel format(List<NewsFetchArticle> newsList, String key) {

        if(key.trim().isEmpty())
            key = "us";

        CountryNewsModel countryNewsModel = new CountryNewsModel();

        countryNewsModel.setCca2(key);
        countryNewsModel.setArticles(newsList);

        Integer count = newsList.size();
        countryNewsModel.setArticleCount(count);

        Flux.fromIterable(newsList)
                .map(NewsFetchArticle::getSentiment)
                .reduce(Double::sum)
                .map(totalSentiment -> count > 0 ? totalSentiment / count : 0.0)
                .subscribe(countryNewsModel::setAverageSentiment);

        RestCountryModel restCountryModel = countryModelMap.get(key);
        countryNewsModel.setCountry(restCountryModel.getCommon());
        countryNewsModel.setLatlng(restCountryModel.getLatlng());

        return countryNewsModel;
    }
}
