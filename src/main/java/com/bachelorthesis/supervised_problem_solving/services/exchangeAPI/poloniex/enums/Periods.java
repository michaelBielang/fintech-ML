package com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.enums;

public enum Periods {

    fiveMinutes(300),
    fifteenMinutes(900),
    thirtyMinutes(1800),
    twoHours(7200),
    fourHours(14400),
    oneDay(86400);

    private final int periodValue;

    Periods(int periodValue) {
        this.periodValue = periodValue;
    }

    public int getPeriodValue() {
        return periodValue;
    }
}
