package com.bachelorthesis.supervised_problem_solving.exchangeAPI.enums;

public enum Periods {

    threeHundred(300),
    nineHundred(900),
    eighteenHundred(1800),
    seventyTwoHundred(7200),
    hundredFortyFourHundred(14400),
    eightHundredSixtyFour(86400);

    private int periodValue;

    Periods(int periodValue) {
        this.periodValue = periodValue;
    }

    public int getPeriodValue() {
        return periodValue;
    }
}
