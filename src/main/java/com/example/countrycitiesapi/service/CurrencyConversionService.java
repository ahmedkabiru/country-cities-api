package com.example.countrycitiesapi.service;

import com.example.countrycitiesapi.model.CurrencyConversionResult;

import java.util.concurrent.ExecutionException;

public interface CurrencyConversionService {

    CurrencyConversionResult currencyConversion(String country, String amount, String targetCurrency) throws ExecutionException, InterruptedException;
}
