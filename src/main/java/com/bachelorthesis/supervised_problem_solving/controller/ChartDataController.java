package com.bachelorthesis.supervised_problem_solving.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@RestController
public class ChartDataController {


    @PostMapping(path = "/chartData/")
    public String entryPoint(@RequestParam @NotBlank String currency, @RequestParam @NotBlank int from,
                             @RequestParam @NotBlank int to,
                             @RequestParam @Min(300) @Max(86400) @NotBlank int timeFrame) {
        return "cool";
    }

    @GetMapping(path = "/chartData/")
    public String getRandomData() {
        return "cool";
    }
}
