package com.bachelorthesis.supervised_problem_solving.services.exchangeAPI;

import com.bachelorthesis.supervised_problem_solving.enums.Indicators;
import com.bachelorthesis.supervised_problem_solving.matlab.Matlab4Regression;
import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.PoloniexApiService;
import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.enums.Periods;
import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.vo.ChartDataVO;
import com.bachelorthesis.supervised_problem_solving.storage.Storage;
import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
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
            final List<ChartDataVO> pastData = poloniexApiService.
                    getChartData(LocalDateTime.now().minusMonths(12), LocalDateTime.now().minusMonths(8), "BTC_ETH", Periods.fourHours);
            final List<ChartDataVO> testData = poloniexApiService.
                    getChartData(LocalDateTime.now().minusMonths(6), LocalDateTime.now().minusMonths(2), "BTC_ETH", Periods.fourHours);
            final Matlab4Regression matlab4Regression = new Matlab4Regression();
            matlab4Regression.calculateSignals(pastData, testData, List.of(Indicators.RSI, Indicators.MACD));
        } catch (IOException | InterruptedException | MatlabConnectionException | MatlabInvocationException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    public void setStorage(Storage storage) {
        this.storage = storage;
    }
}
