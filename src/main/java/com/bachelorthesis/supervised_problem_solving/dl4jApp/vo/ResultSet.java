package com.bachelorthesis.supervised_problem_solving.dl4jApp.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class ResultSet {

    private final LocalDateTime localDateTime;
    private final Map<String, Double> indicatorToValues = new LinkedHashMap<>();
    private final Map<String, Double> returnsToValues = new LinkedHashMap<>();
}
