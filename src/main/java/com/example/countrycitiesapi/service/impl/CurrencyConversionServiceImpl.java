package com.example.countrycitiesapi.service.impl;

import com.example.countrycitiesapi.client.CountryAPIClient;
import com.example.countrycitiesapi.exception.CustomException;
import com.example.countrycitiesapi.model.CurrencyConversionResult;
import com.example.countrycitiesapi.model.CurrencyData;
import com.example.countrycitiesapi.service.CurrencyConversionService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class CurrencyConversionServiceImpl implements CurrencyConversionService {

    final CountryAPIClient countryAPIClient;

    public CurrencyConversionServiceImpl(CountryAPIClient countryAPIClient) {
        this.countryAPIClient = countryAPIClient;
    }

    @Override
    public CurrencyConversionResult currencyConversion(String country, String amount, String targetCurrency) throws ExecutionException, InterruptedException {
        CompletableFuture<CurrencyData> currencyDataFuture =  countryAPIClient.getCurrencyByCountry(country);
        CurrencyData currencyData =  currencyDataFuture.get();
        double exchangeRate = getExchangeRate(currencyData.getCurrency(), targetCurrency);
        if(exchangeRate == 0.0){
            throw  new CustomException(HttpStatus.BAD_REQUEST,String.format("Invalid conversion currency from %s to %s " , targetCurrency, currencyData.getCurrency()));
        }
        double convertedAmount = Double.parseDouble(amount) * exchangeRate;
        String formattedAmount = String.format("%.2f", convertedAmount);
        return CurrencyConversionResult.builder()
                .sourceCurrency(currencyData.getCurrency())
                .targetCurrency(targetCurrency)
                .amount(amount)
                .conversionAmount(formattedAmount)
                .build();
    }

    private Double getExchangeRate(String sourceCurrency, String targetCurrency){
        try (Reader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/exchange_rate.csv"), StandardCharsets.UTF_8))) {
            CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            for (CSVRecord record : csvParser) {
                String baseCurrency = record.get("sourceCurrency");
                String currency = record.get("targetCurrency");
                String rate = record.get("rate");
                if (baseCurrency.equalsIgnoreCase(sourceCurrency) && currency.equalsIgnoreCase(targetCurrency)) {
                    return Double.parseDouble(rate);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0.0;
    }



}
