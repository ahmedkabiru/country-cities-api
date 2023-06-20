package com.example.countrycitiesapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LocationData{
	private String name;
	private String iso2;

	@JsonProperty("long")
	private int longitude;

	@JsonProperty("lat")
	private int latitude;
}