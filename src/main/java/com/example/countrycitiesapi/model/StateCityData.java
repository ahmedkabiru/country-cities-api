package com.example.countrycitiesapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StateCityData{


	private String state;
	private List<String> cities;


}