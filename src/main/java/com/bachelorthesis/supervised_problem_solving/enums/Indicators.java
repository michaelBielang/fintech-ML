package com.bachelorthesis.supervised_problem_solving.enums;

import java.util.List;

public enum Indicators {

    MACD,
    RSI;

    public static List<Indicators> getAllIndicators() {
        return List.of(RSI, MACD);
    }
}
