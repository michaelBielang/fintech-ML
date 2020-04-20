package com.bachelorthesis.supervised_problem_solving.storage;

import com.bachelorthesis.supervised_problem_solving.exchangeAPI.pojo.chartData.ChartDataPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class PoloniexSpecs {

    @Autowired
    EntityManager entityManager;

    public List<ChartDataPojo> getChartData(String criteria) {
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<ChartDataPojo> criteriaQuery = criteriaBuilder.createQuery(ChartDataPojo.class);
        final Root<ChartDataPojo> chartDataPojo = criteriaQuery.from(ChartDataPojo.class);
        criteriaQuery.where(criteriaBuilder.equal(chartDataPojo.get("currency"), "BTC_SIA"));

        Query query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }
}
