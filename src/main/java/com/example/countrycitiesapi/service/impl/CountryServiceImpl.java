package com.example.countrycitiesapi.service.impl;

import com.example.countrycitiesapi.client.CountryAPIClient;
import com.example.countrycitiesapi.client.PopulationAPIClient;
import com.example.countrycitiesapi.model.*;
import com.example.countrycitiesapi.service.CountryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

@Service
public class CountryServiceImpl implements CountryService {

    final PopulationAPIClient populationAPIClient;

    final CountryAPIClient countryAPIClient;

    public CountryServiceImpl(PopulationAPIClient populationAPIClient, CountryAPIClient countryAPIClient) {
        this.populationAPIClient = populationAPIClient;
        this.countryAPIClient = countryAPIClient;
    }

    @Override
    public Object getTopCities(String numberOfCities) throws Exception {
        Map<String, List<String>> result = new LinkedHashMap<>();
        List<String> italyTopCities = populationAPIClient.getCitiesByCountryAndLimit("Italy", numberOfCities);
        List<String> newZealandTopCities = populationAPIClient.getCitiesByCountryAndLimit("New Zealand", numberOfCities);
        List<String> ghanaTopCities = populationAPIClient.getCitiesByCountryAndLimit("Ghana", numberOfCities);
        result.put("Italy", italyTopCities);
        result.put("NewZealand", newZealandTopCities);
        result.put("Ghana", ghanaTopCities);
        return result;
    }

    @Override
    public CountryInfo getCountryInfo(String country) throws ExecutionException, InterruptedException {
       CompletableFuture<PopulationData> populationFuture =  populationAPIClient.getPopulationByCountry(country);
       CompletableFuture<CapitalData>   capitalFuture = countryAPIClient.getCapitalByCountry(country);
       CompletableFuture<LocationData>  locationFuture = countryAPIClient.getLocationByCountry(country);
       CompletableFuture<CurrencyData>  currencyFuture = countryAPIClient.getCurrencyByCountry(country);
      return   CompletableFuture.allOf(populationFuture,capitalFuture, locationFuture, currencyFuture)
               .thenApply(s ->{
                   PopulationData populationData = populationFuture.join();
                   CapitalData capitalData = capitalFuture.join();
                   LocationData locationData = locationFuture.join();
                   CurrencyData currencyData = currencyFuture.join();
                   return  CountryInfo.builder()
                           .capitalCity(capitalData.getCapital())
                           .currency(currencyData.getCurrency())
                           .iso2(currencyData.getIso2())
                           .iso3(currencyData.getIso3())
                           .latitude(String.valueOf(locationData.getLatitude()))
                           .longitude(String.valueOf(locationData.getLongitude()))
                           .populationCounts(populationData.getPopulationCounts())
                           .build();
               }).get();
    }

    @Override
    public List<StateCityData> getStateAndCities(String country) throws ExecutionException, InterruptedException {
        CompletableFuture<List<CountryStateData.StatesItem>> statesFuture = countryAPIClient.getStatesByCountry(country);
        return statesFuture.thenCompose(
                statesItems -> {
                    List<CompletableFuture<List<String>>> cityFutures = new ArrayList<>();
                    for(CountryStateData.StatesItem state: statesItems){
                        CompletableFuture<List<String>> result = countryAPIClient.getCitiesByCountryAndState(country,state.getName());
                        cityFutures.add(result);
                    }
                    CompletableFuture<Void> allCitiesFuture = CompletableFuture.allOf(
                            cityFutures.toArray(new CompletableFuture[0])
                    );
                    return allCitiesFuture.thenApply(v -> {
                        return  IntStream.range(0, statesItems.size()).parallel().
                                mapToObj(index -> new StateCityData(statesItems.get(index).getName(),cityFutures.get(index).join())).toList();
                    });
                }

        ).get();

    }
}
