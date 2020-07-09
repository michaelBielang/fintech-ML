package com.bachelorthesis.supervised_problem_solving.services.exchangeAPI;

import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.enums.Periods;
import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.pojo.chartData.ChartDataVO;
import com.bachelorthesis.supervised_problem_solving.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class Test {

    private Storage storage;

    @Autowired
    PoloniexApiService poloniexApiService;

    public void test() {
        try {
            final List<String> currencies = poloniexApiService.getAvailableCurrenciesAtExchange();
            final List<ChartDataVO> chartDataVOS = poloniexApiService.getChartData(LocalDateTime.now().minusMonths(1), LocalDateTime.now(), currencies.get(0), Periods.eighteenHundred);
            storage.saveChartDate(chartDataVOS);
            System.out.println("SAVED");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    public void setStorage(Storage storage) {
        this.storage = storage;
    }
}
