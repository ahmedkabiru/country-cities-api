package com.example.countrycitiesapi.model;

import lombok.Data;

import java.util.List;

@Data
public class PopulationData{
	private String country;
	private String code;
	private List<PopulationCountsItem> populationCounts;
	private String iso3;
	@Data
	public static class PopulationCountsItem{
		private int year;
		private int value;
	}
}