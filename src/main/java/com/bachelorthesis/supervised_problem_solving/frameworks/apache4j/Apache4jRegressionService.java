package com.bachelorthesis.supervised_problem_solving.frameworks.apache4j;


import com.bachelorthesis.supervised_problem_solving.configuration.RuntimeDataStorage;
import com.bachelorthesis.supervised_problem_solving.enums.Indicators;
import com.bachelorthesis.supervised_problem_solving.services.algos.Algorithms;
import com.bachelorthesis.supervised_problem_solving.services.algos.MatrixService;
import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.vo.ChartDataVO;
import org.apache.spark.SparkConf;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.regression.LinearRegression;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.bachelorthesis.supervised_problem_solving.services.algos.FactorNames.getFactorNames;
import static org.apache.spark.sql.api.r.SQLUtils.createStructField;

@Service
public class Apache4jRegressionService {

    private final RuntimeDataStorage runtimeDatastorage = new RuntimeDataStorage();

    private final static int TRADING_FREQUENCY = 60;

    // delta between bars
    private final static int[] BAR_DELTA = new int[]{5, 10, 15, 20, 25, 30, 60};

    public void calculateSignals(final List<ChartDataVO> chartDataVOList, final List<Indicators> technicalIndicatorsList) {

        initMatrixSetup(chartDataVOList, technicalIndicatorsList);

        final List<String> factorNames = getFactorNames(technicalIndicatorsList, BAR_DELTA);
        final SparkSession spark = getSparkSession();

        // y in Sample
        final List<Double> futureReturns = Algorithms.getReturns(chartDataVOList, TRADING_FREQUENCY);

        final Dataset<Row> dataSet = createMatrix(chartDataVOList, technicalIndicatorsList, factorNames, spark);

        final VectorAssembler assembler = new VectorAssembler()
                .setInputCols(factorNames.toArray(String[]::new))
                .setOutputCol("futureReturns");

        final LinearRegression lr = new LinearRegression()
                .setMaxIter(10)
                .setRegParam(0.3)
                .setElasticNetParam(0.8);

        final Dataset<Double> doubleDataset = spark.createDataset(futureReturns, Encoders.DOUBLE());

        final LinearRegression linearRegression = new LinearRegression();
    }

    private void initMatrixSetup(List<ChartDataVO> chartDataVOList, List<Indicators> technicalIndicatorsList) {
        runtimeDatastorage.findAndSetMaximumMatrixRows(chartDataVOList, technicalIndicatorsList, BAR_DELTA, TRADING_FREQUENCY);
        validateNumberOfDataPoints(chartDataVOList);
    }

    private Dataset<Row> createMatrix(List<ChartDataVO> chartDataVOList, List<Indicators> technicalIndicatorsList, List<String> factorNames, SparkSession spark) {
        final List<Row> rows = MatrixService.getRowList(chartDataVOList, factorNames, BAR_DELTA, technicalIndicatorsList);
        final StructField[] structFields = new StructField[factorNames.size()];

        for (int i = 0; i < factorNames.size(); i++) {
            structFields[i] = createStructField(factorNames.get(i), "double", true);
        }

        final StructType schemata = DataTypes.createStructType(structFields);

        final Dataset<Row> dataSet = spark.createDataFrame(rows, schemata);
        return dataSet;
    }

    private Dataset<Row> addColumnToMatrix(Dataset<Row> dataSet, Dataset<Double> doubleDataset) {
        return dataSet.join(doubleDataset).withColumnRenamed("value", "futureReturns");
    }

    private SparkSession getSparkSession() {
        final SparkConf sparkConf = new SparkConf();
        sparkConf.setMaster("local[2]");
        sparkConf.setAppName("MLA");
        return SparkSession.builder().config(sparkConf).getOrCreate();
    }

    private void validateNumberOfDataPoints(List<ChartDataVO> chartDataVOList) {
        if (chartDataVOList.size() < RuntimeDataStorage.getMatrixRowLength()) {
            throw new IllegalArgumentException("Not enough data. Please deliver more");
        }
    }
}
