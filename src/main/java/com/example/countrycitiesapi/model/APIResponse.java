package com.example.countrycitiesapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResponse<T>{

    Boolean error;

    String  msg;

    T data;

}
