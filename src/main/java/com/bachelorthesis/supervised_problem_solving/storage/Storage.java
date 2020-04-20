package com.bachelorthesis.supervised_problem_solving.storage;

import com.bachelorthesis.supervised_problem_solving.exchangeAPI.pojo.chartData.ChartDataPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class Storage {

    @Autowired
    ChartRepository chartRepository;

    public void saveChartDate(List<ChartDataPojo> chartDataPojos) {
        chartRepository.saveAll(chartDataPojos);
    }

    public void getChartDataHistory(List<ChartDataPojo> chartDataPojos) {
        chartRepository.saveAll(chartDataPojos);
    }
}
