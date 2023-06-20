package com.example.countrycitiesapi.controller;

import com.example.countrycitiesapi.service.CountryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CountryController {

    final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/top-cities")
    public ResponseEntity<?> getTopCities(@RequestParam("number_of_cities") String numOfCities) throws Exception {
        return  ResponseEntity.ok(countryService.getTopCities(numOfCities));
    }

    @GetMapping("/country-info")
    public ResponseEntity<?> getCountryInfo(@RequestParam("country") String county) throws Exception {
        return  ResponseEntity.ok(countryService.getCountryInfo(county));
    }


    @GetMapping("/cities-state")
    public ResponseEntity<?> getStateAndCities(@RequestParam("country") String county) throws Exception {
        return  ResponseEntity.ok(countryService.getStateAndCities(county));
    }


}
