package com.bachelorthesis.supervised_problem_solving.services.exchangeAPI;

import com.bachelorthesis.supervised_problem_solving.services.algos.Algorithms;
import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.PoloniexApiService;
import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.enums.Periods;
import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.vo.ChartDataVO;
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
    private PoloniexApiService poloniexApiService;


    public void testFetchAndSave() {
        try {
            final List<String> currencies = poloniexApiService.getAvailableCurrenciesAtExchange();
            final List<ChartDataVO> chartDataVOS = poloniexApiService.getChartData(LocalDateTime.now().minusMonths(1), LocalDateTime.now(), currencies.get(0), Periods.thirtyMinutes);
            storage.saveChartDate(chartDataVOS);
            System.out.println("SAVED");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testRsi() {
        try {
            final List<ChartDataVO> chartDataVOS = poloniexApiService.getChartData(LocalDateTime.now().minusMonths(1), LocalDateTime.now(), "BTC_ETH", Periods.oneDay);
            System.out.println(chartDataVOS.size());
            System.out.println(Algorithms.getRsi(chartDataVOS, 5).size());
            System.out.println(Algorithms.getMac(chartDataVOS));
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    public void setStorage(Storage storage) {
        this.storage = storage;
    }
}
