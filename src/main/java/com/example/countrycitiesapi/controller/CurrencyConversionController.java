package com.example.countrycitiesapi.controller;

import com.example.countrycitiesapi.model.CurrencyConversionResult;
import com.example.countrycitiesapi.service.CurrencyConversionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class CurrencyConversionController {


    final CurrencyConversionService currencyConversionService;

    public CurrencyConversionController(CurrencyConversionService currencyConversionService) {
        this.currencyConversionService = currencyConversionService;
    }

    @GetMapping("/currency-conversion")
    public ResponseEntity<CurrencyConversionResult> convertCurrency(
            @RequestParam String country,
            @RequestParam String amount,
            @RequestParam String targetCurrency) throws ExecutionException, InterruptedException {
        CurrencyConversionResult currencyConversionResult = currencyConversionService.currencyConversion(country,amount,targetCurrency);
        return ResponseEntity.ok(currencyConversionResult);
    }
}
