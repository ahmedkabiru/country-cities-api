package com.example.countrycitiesapi.service;

import com.example.countrycitiesapi.model.CountryInfo;
import com.example.countrycitiesapi.model.StateCityData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface CountryService {

    Object getTopCities(String numberOfCities) throws Exception;

    CountryInfo getCountryInfo(String country) throws ExecutionException, InterruptedException;

    List<StateCityData> getStateAndCities(String country) throws ExecutionException, InterruptedException;
}
