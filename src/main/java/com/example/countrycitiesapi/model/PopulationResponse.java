package com.example.countrycitiesapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PopulationResponse{
	private String country;
	private String city;
	private List<PopulationCountsItem> populationCounts;

	@Data
	public static class PopulationCountsItem{
		private String reliabilty;
		private String year;
		private String sex;
		private String value;
	}
}