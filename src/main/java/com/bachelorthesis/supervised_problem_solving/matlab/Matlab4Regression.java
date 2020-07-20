package com.bachelorthesis.supervised_problem_solving.matlab;

import com.bachelorthesis.supervised_problem_solving.configuration.RuntimeDataStorage;
import com.bachelorthesis.supervised_problem_solving.enums.Indicators;
import com.bachelorthesis.supervised_problem_solving.services.algos.Algorithms;
import com.bachelorthesis.supervised_problem_solving.services.algos.ApacheMatrixService;
import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.vo.ChartDataVO;
import lombok.val;
import matlabcontrol.*;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import scala.collection.mutable.StringBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.bachelorthesis.supervised_problem_solving.services.algos.FactorNames.getFactorNames;
import static org.apache.spark.sql.api.r.SQLUtils.createStructField;

public class Matlab4Regression {

    private final RuntimeDataStorage runtimeDatastorage = new RuntimeDataStorage();

    private static final int TRADING_FREQUENCY = 60;

    private SparkSession sparkSession;

    private List<String> factorNames;

    // delta between bars
    private static final int[] BAR_DELTA = new int[]{5, 10, 15, 20, 25, 30, 60};

    public void calculateSignals(final List<ChartDataVO> pastData, List<ChartDataVO> testData, final List<Indicators> technicalIndicatorsList) throws IOException, InterruptedException, MatlabInvocationException, MatlabConnectionException {

        factorNames = getFactorNames(technicalIndicatorsList, BAR_DELTA);
        this.sparkSession = setupSpark();

        // y in Sample
        createPastData(pastData, technicalIndicatorsList);
        createTestData(testData, technicalIndicatorsList);

        runMatlab();
        return;
    }

    private void createTestData(List<ChartDataVO> testData, List<Indicators> technicalIndicatorsList) throws IOException {
        setupEnvironment(testData, technicalIndicatorsList);

        final List<Double> futureReturns = Algorithms.getReturns(testData, TRADING_FREQUENCY);

        final Dataset<Row> indicatorDataSet = getIndicatorDataSet(testData, technicalIndicatorsList, factorNames);
        saveDataToCSV(indicatorDataSet, "testData", true);
        //saveDataToCSV(indicatorDataSet, "fullTable", true); not yet necessary

        final Dataset<Double> futureReturnsDataSet = sparkSession.createDataset(futureReturns, Encoders.DOUBLE());
        saveDataToCSV(futureReturnsDataSet, "returns", true);
    }

    private void createPastData(List<ChartDataVO> pastData, List<Indicators> technicalIndicatorsList) throws IOException {
        setupEnvironment(pastData, technicalIndicatorsList);

        final List<Double> futureReturns = Algorithms.getReturns(pastData, TRADING_FREQUENCY);

        final Dataset<Row> indicatorDataSet = getIndicatorDataSet(pastData, technicalIndicatorsList, factorNames);
        saveDataToCSV(indicatorDataSet, "trainingData", true);
        //saveDataToCSV(indicatorDataSet, "fullTable", true); not yet necessary

        final Dataset<Double> futureReturnsDataSet = sparkSession.createDataset(futureReturns, Encoders.DOUBLE());
        saveDataToCSV(futureReturnsDataSet, "futureReturns", true);
    }

    public void runMatlab() throws MatlabConnectionException, MatlabInvocationException {

        // create proxy
        MatlabProxyFactoryOptions.Builder builder = new MatlabProxyFactoryOptions.Builder();

        MatlabProxyFactory factory = new MatlabProxyFactory(builder.build());
        // get the proxy
        MatlabProxy proxy = factory.getProxy();

        // call user-defined function (must be on the path)
        proxy.eval("addpath('" + getPath().toString() + "')");
        proxy.feval("BA_Michael_Bielang_Regression");

        // close connection
        proxy.disconnect();
    }

    private StringBuilder getPath() {
        final StringBuilder path = new StringBuilder(System.getProperty("user.dir").replace("\\", "\\\\"));
        path.append("\\matlab");
        return path;
    }

    private Dataset<Row> getIndicatorDataSet(List<ChartDataVO> chartDataVOList, List<Indicators> technicalIndicatorsList, List<String> factorNames) {
        List<Row> rows = ApacheMatrixService.getRowList(chartDataVOList, factorNames, BAR_DELTA, technicalIndicatorsList);
        final StructField[] structFields = new StructField[factorNames.size()];

        for (int i = 0; i < factorNames.size(); i++) {
            structFields[i] = createStructField(factorNames.get(i), "double", true);
        }

        final StructType schemata = DataTypes.createStructType(structFields);

        return sparkSession.createDataFrame(rows, schemata);
    }

    private SparkSession setupSpark() {
        SparkConf sparkConf = new SparkConf();
        sparkConf.setMaster("local[2]");
        sparkConf.setAppName("MLA");
        return SparkSession.builder().config(sparkConf).getOrCreate();
    }

    private void setupEnvironment(List<ChartDataVO> chartDataVOList, List<Indicators> technicalIndicatorsList) {
        runtimeDatastorage.findAndSetMaximumMatrixRows(chartDataVOList, technicalIndicatorsList, BAR_DELTA, TRADING_FREQUENCY);
        validateNumberOfDataPoints(chartDataVOList);
    }

    private void saveDataToCSV(Dataset<?> dataSet, String dataType, boolean withHeader) throws IOException {
        dataSet.coalesce(1).
                write().
                mode("overwrite").
                format("com.databricks.spark.csv").
                option("header", String.valueOf(withHeader)).
                save("tmp.csv");
        val fs = FileSystem.get(sparkSession.sparkContext().hadoopConfiguration());
        File dir = new File(System.getProperty("user.dir") + "/tmp.csv/");
        File[] files = dir.listFiles((d, name) -> name.endsWith(".csv"));
        fs.rename(new Path(files[0].toURI()), new Path(System.getProperty("user.dir") + "/matlab/" + dataType + ".csv"));
        fs.delete(new Path(System.getProperty("user.dir") + "/tmp.csv/"), true);
    }

    private void validateNumberOfDataPoints(List<ChartDataVO> chartDataVOList) {
        if (chartDataVOList.size() < RuntimeDataStorage.getMatrixRowLength()) {
            throw new IllegalArgumentException("Not enough data. Please deliver more");
        }
    }
}
