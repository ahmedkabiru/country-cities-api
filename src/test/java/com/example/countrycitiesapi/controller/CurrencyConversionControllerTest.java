package com.example.countrycitiesapi.controller;

import com.example.countrycitiesapi.model.CurrencyConversionResult;
import com.example.countrycitiesapi.service.CurrencyConversionService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(controllers = CurrencyConversionController.class)
class CurrencyConversionControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    CurrencyConversionService currencyConversionService;

    @Test
    void convertCurrency() throws Exception {
        CurrencyConversionResult currencyConversionResult = CurrencyConversionResult.builder()
                .amount("2000.0")
                .targetCurrency("NGN")
                .sourceCurrency("EUR")
                .conversionAmount("986120.00")
                .build();
        given(currencyConversionService.currencyConversion("Italy", "2000.0","NGN"))
                .willReturn(currencyConversionResult);
        this.mvc.perform(MockMvcRequestBuilders.get("/currency-conversion")
                        .param("country", "Italy")
                        .param("amount", String.valueOf(2000.0))
                        .param("targetCurrency", "NGN")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.conversionAmount", Matchers.is(currencyConversionResult.getConversionAmount())));

    }
}