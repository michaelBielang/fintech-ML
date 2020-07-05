package com.bachelorthesis.supervised_problem_solving.exchangeAPI;

import com.bachelorthesis.supervised_problem_solving.exchangeAPI.enums.Periods;
import com.bachelorthesis.supervised_problem_solving.exchangeAPI.pojo.chartData.ChartDataVO;
import com.bachelorthesis.supervised_problem_solving.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class Test {

    private Storage storage;
    private Poloniex poloniex = new Poloniex();

    public void test() {
        try {
            final List<String> currencies = poloniex.getAvailableCurrenciesAtExchange();
            final List<ChartDataVO> chartDataVOS = poloniex.getChartData(LocalDateTime.now().minusMonths(3), LocalDateTime.now(), currencies.get(0), Periods.eighteenHundred.getPeriodValue());
            storage.saveChartDate(chartDataVOS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    public void setStorage(Storage storage) {
        this.storage = storage;
    }
}
