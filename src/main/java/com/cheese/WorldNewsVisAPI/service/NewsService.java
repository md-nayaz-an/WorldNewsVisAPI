package com.cheese.WorldNewsVisAPI.service;

import com.cheese.WorldNewsVisAPI.Logger;
import com.cheese.WorldNewsVisAPI.models.CountryNewsModel;
import com.cheese.WorldNewsVisAPI.models.NewsFetchArticle;
import com.cheese.WorldNewsVisAPI.models.NewsFetchData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

@Service
public class NewsService {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JsonResponseFormatter jsonResponseFormatter;

    @Autowired
    WorldNewsAPIFetch worldNewsAPIFetch;

    public String getAllNews() {
        return "test";
    }

    public List<CountryNewsModel> searchNews(String queryString) throws IOException {
    /*
        String filePath = "src/main/java/com/cheese/WorldNewsVisAPI/sampleData/newsData2.json";
        String jsonData = Files.readString(Paths.get(filePath));

        NewsFetchData newsFetchData = objectMapper.readValue(jsonData, NewsFetchData.class);
    */
        NewsFetchData newsFetchData = worldNewsAPIFetch.fetch(queryString.trim().replace(' ', '+'));
        return jsonResponseFormatter.format(newsFetchData.getNews());
    }
}