package com.cheese.WorldNewsVisAPI.models;

import lombok.Data;

import java.util.List;

@Data
public class NewsFetchData {
    private int offset;
    private int number;
    private int available;
    private List<NewsFetchArticle> news;
}
