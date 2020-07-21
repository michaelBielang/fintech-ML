package com.bachelorthesis.supervised_problem_solving.frameworks.dl4jApp.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Data
public class ResultSet {

    private final LocalDateTime localDateTime;
    private final List<Double> indicatorValues = new LinkedList<>();
    private final List<Double> returns = new LinkedList<>();
}
