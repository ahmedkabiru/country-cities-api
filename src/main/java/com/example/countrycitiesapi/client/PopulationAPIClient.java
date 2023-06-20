package com.example.countrycitiesapi.client;

import com.example.countrycitiesapi.exception.CustomException;
import com.example.countrycitiesapi.model.APIResponse;
import com.example.countrycitiesapi.model.PopulationData;
import com.example.countrycitiesapi.model.PopulationResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
public class PopulationAPIClient {

    final WebClient webClient;

    final ObjectMapper objectMapper;

    public PopulationAPIClient(WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }


    public List<String> getCitiesByCountryAndLimit(String country, String limit) throws Exception {
        MultiValueMap<String,String> queryParameter = new LinkedMultiValueMap<>();
        queryParameter.add("limit", limit);
        queryParameter.add("order", "dsc");
        queryParameter.add("orderBy", "populationCounts");
        queryParameter.add("country", country);
        ResponseEntity<?> responseEntity = webClient.get().uri(uriBuilder -> uriBuilder.path("population/cities/filter/q")
                            .queryParams(queryParameter)
                            .build()
                    )
                    .retrieve()
                    .toEntity(String.class)
                    .block();
            if (responseEntity!= null && responseEntity.getStatusCode() == HttpStatus.OK) {
                TypeReference<APIResponse<PopulationResponse[]>> typeRef = new TypeReference<>() {};
                APIResponse<PopulationResponse[]> jsonResponse  =  objectMapper.readValue(Objects.requireNonNull(responseEntity.getBody()).toString(), typeRef);
                return Arrays.stream(jsonResponse.getData()).map(PopulationResponse::getCity).toList();
            }else{
                if(responseEntity!= null) {
                    throw new CustomException(HttpStatus.valueOf(responseEntity.getStatusCode().value()), Objects.requireNonNull(responseEntity.getBody()).toString());
                }
            }
            return Collections.emptyList();
    }


    public  CompletableFuture<PopulationData>  getPopulationByCountry(String country){
        MultiValueMap<String,String> queryParameter = new LinkedMultiValueMap<>();
        queryParameter.add("country", country);
       return webClient.get().uri(uriBuilder -> uriBuilder.path("population/q")
                        .queryParams(queryParameter)
                        .build()
                )
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<APIResponse<PopulationData>>() {})
                .map(APIResponse::getData)
                .toFuture();
    }



}
