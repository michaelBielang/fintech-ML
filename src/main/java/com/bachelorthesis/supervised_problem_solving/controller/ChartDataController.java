package com.bachelorthesis.supervised_problem_solving.controller;

import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.PoloniexApiService;
import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.pojo.chartData.ChartDataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@RestController
public class ChartDataController {

    @Autowired
    private PoloniexApiService poloniexApiService;

    @GetMapping(path = "/api/chartData")
    public List<ChartDataVO> requestDistinctChartData(@RequestParam @NotBlank final String currency,
                                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDateTime from,
                                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDateTime to,
                                                      @RequestParam(required = false) @Min(300) @Max(86400) final Integer timeFrame) throws IOException, InterruptedException {
        return new LinkedList<>();
        //return poloniexApiService.getChartData(from, to, currency, Periods.valueOf(String.valueOf(timeFrame)));
    }
}

