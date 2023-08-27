package com.cheese.WorldNewsVisAPI.models;

import lombok.Data;

@Data
public class NewsFetchArticle {
    private String title;
    private String url;
    private String publish_date;
    private String author;
    private String source_country;
    private double sentiment;

    private String text;
    private String language;
    private String image;
}
