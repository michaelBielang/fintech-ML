package com.bachelorthesis.supervised_problem_solving.storage;

import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.pojo.chartData.ChartDataVO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PoloniexChartDataVOSRepository extends CrudRepository<ChartDataVO, Long> {
}
