package com.bachelorthesis.supervised_problem_solving.exchangeAPI;

import com.bachelorthesis.supervised_problem_solving.exchangeAPI.enums.Periods;
import com.bachelorthesis.supervised_problem_solving.exchangeAPI.pojo.chartData.ChartDataVO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PoloniexApiServiceTest {

    private static PoloniexApiService poloniexApiService;
    private static List<ChartDataVO> chartDataVOList;
    private static List<String> currencyList;

    @BeforeAll
    public static void setup() throws IOException, InterruptedException {
        PoloniexApiServiceTest.poloniexApiService = new PoloniexApiService();
        PoloniexApiServiceTest.currencyList = poloniexApiService.getAvailableCurrenciesAtExchange();
        PoloniexApiServiceTest.chartDataVOList = poloniexApiService.getChartData(LocalDateTime.now().minusMonths(1), LocalDateTime.now(), currencyList.get(0), Periods.nineHundred);
    }

    @Test
    void getChartData() {
        assertTrue(chartDataVOList.size() > 0);
        assertEquals(chartDataVOList.get(0).getCurrency(), currencyList.get(0));
    }

    @Test
    void getAvailableCurrenciesAtExchange() {
        assertTrue(currencyList.size() > 0);
        assertTrue(currencyList.contains("USDT_BTC"));
    }
}
