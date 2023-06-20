package com.example.countrycitiesapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CountryStateData{
	private String name;
	private String iso2;
	private String iso3;
	private List<StatesItem> states;

	@Data
	public static class StatesItem{
		private String name;

		@JsonProperty("state_code")
		private String stateCode;
	}

}