package com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.vo;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class Currency {

    private Map<String, NestedCurrency> nestedCurrencyMap;

    @JsonAnySetter
    public void setMap(String key, NestedCurrency value) {
        if (nestedCurrencyMap == null) {
            nestedCurrencyMap = new LinkedHashMap<>();
        }
        nestedCurrencyMap.put(key, value);
    }

}
