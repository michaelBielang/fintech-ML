package com.bachelorthesis.supervised_problem_solving.exchangeAPI.pojo.chartData;

import lombok.Data;

@Data
public class ChartDataPojo {

    private String date;
    private String high;
    private String low;
    private String open;
    private String close;
    private String volume;
    private String quoteVolume;
    private String weightedAverage;
}
