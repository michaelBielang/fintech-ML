package com.bachelorthesis.supervised_problem_solving.controller;

import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.pojo.chartData.ChartDataVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.LinkedList;
import java.util.List;

@RestController
public class ChartDataController {


    @PostMapping(path = "/chartData")
    public List<ChartDataVO> requestDistinctChartData(@RequestParam @NotBlank String currency, @RequestParam @NotBlank int from,
                                                      @RequestParam @NotBlank int to,
                                                      @RequestParam @Min(300) @Max(86400) @NotBlank int timeFrame) {
        return new LinkedList<>();
    }

    @PostMapping(path = "/chartData")
    public List<ChartDataVO> requestCurrencyChartData(@RequestParam @NotBlank String currency) {
        return new LinkedList<>();
    }

    @GetMapping(path = "/chartData/")
    public String getAllChartData() {
        return "cool";
    }
}

