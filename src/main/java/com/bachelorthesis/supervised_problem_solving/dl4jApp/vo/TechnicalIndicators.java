package com.bachelorthesis.supervised_problem_solving.dl4jApp.vo;

import lombok.Data;

import java.util.Optional;

@Data
public class TechnicalIndicators {

    final private String signalName;
    final private Optional<Integer> timeFrame;

    // bid, ask, mid
    final private String priceType;
}
