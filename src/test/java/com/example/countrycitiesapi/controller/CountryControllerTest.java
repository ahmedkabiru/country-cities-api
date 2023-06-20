package com.example.countrycitiesapi.controller;

import com.example.countrycitiesapi.model.CountryInfo;
import com.example.countrycitiesapi.model.StateCityData;
import com.example.countrycitiesapi.service.CountryService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = CountryController.class)
class CountryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    CountryService countryService;

    @Test
    void getTopCities() throws Exception {
        Map<String, List<String>> result = new LinkedHashMap<>();
        result.put("Italy", List.of("Milan"));
        result.put("NewZealand", List.of("Auckland"));
        result.put("Ghana", List.of("Accra"));
        given(countryService.getTopCities("10")).willReturn(result);
        this.mvc.perform(MockMvcRequestBuilders.get("/top-cities")
                        .param("number_of_cities", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.Italy",Matchers.hasItem("Milan")))
                .andExpect(jsonPath("$.NewZealand",Matchers.hasItem("Auckland")))
                .andExpect(jsonPath("$.Ghana",Matchers.hasItem("Accra")));
    }

    @Test
    void getCountryInfo() throws Exception {
        CountryInfo countryInfo = CountryInfo.builder()
                .capitalCity("Abuja")
                .currency("NGN")
                .longitude("8")
                .latitude("10")
                .iso2("NG")
                .iso3("NGA")
                .populationCounts(Collections.emptyList())
                .build();
        String country = "Nigeria";
        given(countryService.getCountryInfo(country)).willReturn(countryInfo);
        this.mvc.perform(MockMvcRequestBuilders.get("/country-info")
                        .param("country", country)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.capital_city",Matchers.is(countryInfo.getCapitalCity())))
                .andExpect(jsonPath("$.currency",Matchers.is(countryInfo.getCurrency())))
                .andExpect(jsonPath("$.iso2",Matchers.is(countryInfo.getIso2())));


    }

    @Test
    void getStateAndCities() throws Exception {
        String country = "Nigeria";
        List<StateCityData> cityDataList = new LinkedList<>();
        cityDataList.add(new StateCityData("Lagos", List.of("Ikotun","Ikeja")));
        given(countryService.getStateAndCities(country)).willReturn(cityDataList);
        this.mvc.perform(MockMvcRequestBuilders.get("/cities-state")
                        .param("country", country)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)));



    }
}