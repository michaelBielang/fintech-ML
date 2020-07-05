package com.bachelorthesis.supervised_problem_solving.storage;

import com.bachelorthesis.supervised_problem_solving.exchangeAPI.pojo.chartData.ChartDataVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class Storage {

    @Autowired
    ChartRepository chartRepository;

    public void saveChartDate(List<ChartDataVO> chartDataVOS) {
        chartRepository.saveAll(chartDataVOS);
    }

    public void getChartDataHistory(List<ChartDataVO> chartDataVOS) {
        chartRepository.saveAll(chartDataVOS);
    }
}
