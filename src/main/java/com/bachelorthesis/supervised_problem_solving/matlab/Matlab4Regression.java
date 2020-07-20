package com.bachelorthesis.supervised_problem_solving.matlab;

import com.bachelorthesis.supervised_problem_solving.configuration.RuntimeDataStorage;
import com.bachelorthesis.supervised_problem_solving.enums.Indicators;
import com.bachelorthesis.supervised_problem_solving.services.algos.Algorithms;
import com.bachelorthesis.supervised_problem_solving.services.algos.ApacheMatrixService;
import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.vo.ChartDataVO;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.List;

import static com.bachelorthesis.supervised_problem_solving.services.algos.FactorNames.getFactorNames;
import static org.apache.spark.sql.api.r.SQLUtils.createStructField;

public class Matlab4Regression {


    private final RuntimeDataStorage runtimeDatastorage = new RuntimeDataStorage();

    private final int tradingFrequency = 60;

/*
    // delta between bars
    private final int[] barDelta = new int[]{5, 10, 15, 20, 25, 30, 60};
*/

    // delta between bars
    private final int[] barDelta = new int[]{5, 10, 15, 20, 25, 30, 60};

    public void calculateSignals(final List<ChartDataVO> chartDataVOList, final List<Indicators> technicalIndicatorsList) {

        runtimeDatastorage.findAndSetMaximumMatrixRows(chartDataVOList, technicalIndicatorsList, barDelta, tradingFrequency);
        validateNumberOfDataPoints(chartDataVOList);

        final List<String> factorNames = getFactorNames(technicalIndicatorsList, barDelta);
        SparkConf sparkConf = new SparkConf();
        sparkConf.setMaster("local[2]");
        sparkConf.setAppName("MLA");
        SparkSession spark = SparkSession.builder().config(sparkConf).getOrCreate();

        // fill matrix with predictors

        // y in Sample
        final List<Double> futureReturns = Algorithms.getReturns(chartDataVOList, tradingFrequency);

        List<Row> rows = ApacheMatrixService.getRowList(chartDataVOList, factorNames, barDelta, technicalIndicatorsList);
        final StructField[] structFields = new StructField[factorNames.size()];

        for (int i = 0; i < factorNames.size(); i++) {
            structFields[i] = createStructField(factorNames.get(i), "double", true);
        }

        final StructType schemata = DataTypes.createStructType(structFields);

        Dataset<Row> dataSet = spark.createDataFrame(rows, schemata);

        // add column to ds

        Dataset<Double> doubleDataset = spark.createDataset(futureReturns, Encoders.DOUBLE());
        dataSet = dataSet.join(doubleDataset).withColumnRenamed("value", "futureReturns");

        dataSet.show();
        dataSet.coalesce(1).
                write().
                format("com.databricks.spark.csv").
                option("header", "false").
                mode("overwrite").
                save("bangalalala.csv");
    }


    private void validateNumberOfDataPoints(List<ChartDataVO> chartDataVOList) {
        if (chartDataVOList.size() < RuntimeDataStorage.getMatrixRowLength()) {
            throw new IllegalArgumentException("Not enough data. Please deliver more");
        }
    }
}
