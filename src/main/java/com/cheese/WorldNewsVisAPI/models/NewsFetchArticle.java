package com.cheese.WorldNewsVisAPI.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class NewsFetchArticle {
    @Id
    private long id;
    private String title;
    private String url;
    private String publish_date;
    private String author;
    private String source_country;
    private double sentiment;
}
