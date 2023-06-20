package com.example.countrycitiesapi.client;

import com.example.countrycitiesapi.exception.CustomException;
import com.example.countrycitiesapi.model.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class CountryAPIClient {

    final WebClient webClient;

    public CountryAPIClient(WebClient webClient) {
        this.webClient = webClient;
    }


    public CompletableFuture<LocationData> getLocationByCountry(String country){
        MultiValueMap<String,String> queryParameter = new LinkedMultiValueMap<>();
        queryParameter.add("country", country);
        return webClient.get().uri(uriBuilder -> uriBuilder.path("positions/q")
                        .queryParams(queryParameter)
                        .build()
                )
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<APIResponse<LocationData>>() {})
                .map(APIResponse::getData)
                .toFuture();
    }


    public CompletableFuture<CapitalData> getCapitalByCountry(String country){
        MultiValueMap<String,String> queryParameter = new LinkedMultiValueMap<>();
        queryParameter.add("country", country);
        return webClient.get().uri(uriBuilder -> uriBuilder.path("capital/q")
                        .queryParams(queryParameter)
                        .build()
                )
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<APIResponse<CapitalData>>() {})
                .map(APIResponse::getData)
                .toFuture();
    }


    public CompletableFuture<CurrencyData> getCurrencyByCountry(String country){
        MultiValueMap<String,String> queryParameter = new LinkedMultiValueMap<>();
        queryParameter.add("country", country);
        return webClient.get().uri(uriBuilder -> uriBuilder.path("currency/q")
                        .queryParams(queryParameter)
                        .build()
                )
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> {
                 return response.bodyToMono(String.class)
                    .flatMap(errorBody -> {
                        return Mono.error(new CustomException(HttpStatus.valueOf(response.statusCode().value()),errorBody));
                    });
                })
                .bodyToMono(new ParameterizedTypeReference<APIResponse<CurrencyData>>() {})
                .map(APIResponse::getData)
                .toFuture();
    }

    public CompletableFuture<List<CountryStateData.StatesItem>> getStatesByCountry(String country){
        MultiValueMap<String,String> queryParameter = new LinkedMultiValueMap<>();
        queryParameter.add("country", country);
        return webClient.get().uri(uriBuilder -> uriBuilder.path("states/q")
                        .queryParams(queryParameter)
                        .build()
                )
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<APIResponse<CountryStateData>>() {})
                .map(s -> s.getData().getStates())
                .toFuture();
    }


    public CompletableFuture<List<String>> getCitiesByCountryAndState(String country, String state){
        MultiValueMap<String,String> queryParameter = new LinkedMultiValueMap<>();
        queryParameter.add("country", country);
        queryParameter.add("state", state);
        return webClient.get().uri(uriBuilder -> uriBuilder.path("state/cities/q")
                        .queryParams(queryParameter)
                        .build()
                )
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<APIResponse<List<String>>>() {})
                .map(APIResponse::getData)
                .toFuture()
                .exceptionally(ex -> {
                    if (ex instanceof WebClientException) {
                        return Collections.emptyList();
                    } else {
                        throw new RuntimeException("Error occurred while fetching cities for state: " + state, ex);
                    }
                });
    }



}

