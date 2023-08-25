package com.cheese.WorldNewsVisAPI.models;

import lombok.Data;

import java.util.List;

@Data
public class CountryNewsModel {
        private String cca2;
        private List<NewsFetchArticle> articles; // List of articles for the country
        private long articleCount; // Number of articles
        private double averageSentiment;
        private String country;
        private double[] latlng;
}
