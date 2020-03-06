package com.bachelorthesis.supervised_problem_solving.storage;

import com.bachelorthesis.supervised_problem_solving.exchangeAPI.pojo.chartData.ChartDataPojo;
import org.springframework.data.repository.CrudRepository;

public interface ChartRepository extends CrudRepository<ChartDataPojo, Long> {
}
