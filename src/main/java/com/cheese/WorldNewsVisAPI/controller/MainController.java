package com.cheese.WorldNewsVisAPI.controller;

import com.cheese.WorldNewsVisAPI.models.CountryNewsModel;
import com.cheese.WorldNewsVisAPI.models.NewsFetchArticle;
import com.cheese.WorldNewsVisAPI.models.NewsFetchData;
import com.cheese.WorldNewsVisAPI.service.NewsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

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

    @GetMapping(value = "search", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> searchNews(@RequestParam String queryString) {
        return newsService.searchNewsStream(queryString);
    }

    @GetMapping(value = "search/test", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> searchNewsTest(@RequestParam String queryString) throws IOException {
        return newsService.searchNewsTestStream(queryString);
    }
}
