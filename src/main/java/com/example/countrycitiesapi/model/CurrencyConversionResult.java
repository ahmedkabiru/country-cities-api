package com.example.countrycitiesapi.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrencyConversionResult {

    String targetCurrency;

    String sourceCurrency;

    String amount;

    String conversionAmount;
}
