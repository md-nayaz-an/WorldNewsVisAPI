package com.cheese.WorldNewsVisAPI.controller;

import com.cheese.WorldNewsVisAPI.models.CountryNewsModel;
import com.cheese.WorldNewsVisAPI.models.NewsFetchArticle;
import com.cheese.WorldNewsVisAPI.models.NewsFetchData;
import com.cheese.WorldNewsVisAPI.service.NewsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("news")
public class MainController {

    @Autowired
    NewsService newsService;

    @GetMapping("all")
    public String getAllNews() {
        return newsService.getAllNews();
    }

    @GetMapping("search")
    public List<CountryNewsModel> searchNews(@RequestParam String queryString) throws IOException {
        return newsService.searchNews(queryString);
    }
}
