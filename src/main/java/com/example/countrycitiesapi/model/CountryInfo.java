package com.example.countrycitiesapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CountryInfo{

	@JsonProperty("capital_city")
	private String capitalCity;

	@JsonProperty("populationCounts")
	private List<PopulationData.PopulationCountsItem> populationCounts;

	@JsonProperty("currency")
	private String currency;

	@JsonProperty("longitude")
	private String longitude;


	@JsonProperty("latitude")
	private String latitude;

	@JsonProperty("iso2")
	private String iso2;


	@JsonProperty("iso3")
	private String iso3;



}