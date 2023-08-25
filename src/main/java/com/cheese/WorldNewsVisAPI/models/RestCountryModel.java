package com.cheese.WorldNewsVisAPI.models;

import lombok.Data;

import java.util.List;

@Data
public class RestCountryModel {
    private String cca2;
    private NameInfo name;
    private double[] latlng;

    public String getCommon() {
        return name.getCommon();
    }
}

@Data
class NameInfo{
    private String common;
}
