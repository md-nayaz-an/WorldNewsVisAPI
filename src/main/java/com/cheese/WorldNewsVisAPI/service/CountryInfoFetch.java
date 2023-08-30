package com.cheese.WorldNewsVisAPI.service;

import com.cheese.WorldNewsVisAPI.models.RestCountryModel;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CountryInfoFetch {
    public RestCountryModel fetch(String countryCode) {
        WebClient webClient = WebClient.create("https://restcountries.com/v3.1");

        try {
            return webClient.get()
                    .uri(
                            "/alpha/" + countryCode
                                    + "?fields=name,cca2,latlng"
                    )
                    .retrieve()
                    .bodyToMono(RestCountryModel.class)
                    .block();
        } catch (Exception ex) {
            return webClient.get()
                    .uri(
                            "/alpha/us"
                                    + "?fields=name,cca2,latlng"
                    )
                    .retrieve()
                    .bodyToMono(RestCountryModel.class)
                    .block();
        }
    }
}
