package com.bachelorthesis.supervised_problem_solving.storage;

import com.bachelorthesis.supervised_problem_solving.exchangeAPI.pojo.chartData.ChartDataVO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChartRepository extends CrudRepository<ChartDataVO, Long> {
}
