package com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class NestedCurrency {

    private int id;
    private String name;
    private double txFee;
    private int minConf;
    private String depositAddress;
    private int disabled;
    private int delisted;
    private int frozen;

}
