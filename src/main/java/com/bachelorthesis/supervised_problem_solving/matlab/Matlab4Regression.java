package com.bachelorthesis.supervised_problem_solving.matlab;

import com.bachelorthesis.supervised_problem_solving.configuration.RuntimeDataStorage;
import com.bachelorthesis.supervised_problem_solving.enums.Indicators;
import com.bachelorthesis.supervised_problem_solving.services.algos.Algorithms;
import com.bachelorthesis.supervised_problem_solving.services.algos.ApacheMatrixService;
import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.vo.ChartDataVO;
import lombok.val;
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
import org.n52.matlab.control.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.bachelorthesis.supervised_problem_solving.services.algos.FactorNames.getFactorNames;
import static org.apache.spark.sql.api.r.SQLUtils.createStructField;

public class Matlab4Regression {

    private final RuntimeDataStorage runtimeDatastorage = new RuntimeDataStorage();

    private static final int TRADING_FREQUENCY = 60;

    // delta between bars
    private static final int[] BAR_DELTA = new int[]{5, 10, 15, 20, 25, 30, 60};

    public void calculateSignals(final List<ChartDataVO> chartDataVOList, final List<Indicators> technicalIndicatorsList) throws IOException, InterruptedException, MatlabInvocationException, MatlabConnectionException {

        init(chartDataVOList, technicalIndicatorsList);
        final List<String> factorNames = getFactorNames(technicalIndicatorsList, BAR_DELTA);
        final SparkSession spark = setupSpark();

        // y in Sample
        final List<Double> futureReturns = Algorithms.getReturns(chartDataVOList, TRADING_FREQUENCY);

        final Dataset<Row> indicatorDataSet = getIndicatorDataSet(chartDataVOList, technicalIndicatorsList, factorNames, spark);
        saveDataToCSV(spark, indicatorDataSet, "indicatorData", false);
        saveDataToCSV(spark, indicatorDataSet, "fullTable", true);

        final Dataset<Double> futureReturnsDataSet = spark.createDataset(futureReturns, Encoders.DOUBLE());
        saveDataToCSV(spark, futureReturnsDataSet, "futureReturns", false);

        runMatlab();
        return;
    }

    public void runMatlab() throws MatlabConnectionException, MatlabInvocationException {

        // create proxy
        MatlabProxyFactoryOptions options =
                new MatlabProxyFactoryOptions.Builder()
                        .setUsePreviouslyControlledSession(true)
                        .build();

        MatlabProxyFactory factory = new MatlabProxyFactory(options);
        MatlabProxy proxy = factory.getProxy();

        // call builtin function
        proxy.eval("disp('hello world')");

        // call user-defined function (must be on the path)
        proxy.feval(System.getProperty("user.dir") + "/matlab/" + "BA_Michael_Bielang_Regression.m");

        // close connection
        proxy.disconnect();
    }

    private Dataset<Row> getIndicatorDataSet(List<ChartDataVO> chartDataVOList, List<Indicators> technicalIndicatorsList, List<String> factorNames, SparkSession spark) {
        List<Row> rows = ApacheMatrixService.getRowList(chartDataVOList, factorNames, BAR_DELTA, technicalIndicatorsList);
        final StructField[] structFields = new StructField[factorNames.size()];

        for (int i = 0; i < factorNames.size(); i++) {
            structFields[i] = createStructField(factorNames.get(i), "double", true);
        }

        final StructType schemata = DataTypes.createStructType(structFields);

        return spark.createDataFrame(rows, schemata);
    }

    private SparkSession setupSpark() {
        SparkConf sparkConf = new SparkConf();
        sparkConf.setMaster("local[2]");
        sparkConf.setAppName("MLA");
        return SparkSession.builder().config(sparkConf).getOrCreate();
    }

    private void init(List<ChartDataVO> chartDataVOList, List<Indicators> technicalIndicatorsList) {
        runtimeDatastorage.findAndSetMaximumMatrixRows(chartDataVOList, technicalIndicatorsList, BAR_DELTA, TRADING_FREQUENCY);
        validateNumberOfDataPoints(chartDataVOList);
    }

    private void saveDataToCSV(SparkSession spark, Dataset<?> dataSet, String dataType, boolean withHeader) throws IOException {
        dataSet.coalesce(1).
                write().
                mode("overwrite").
                format("com.databricks.spark.csv").
                option("header", String.valueOf(withHeader)).
                save("tmp.csv");
        val fs = FileSystem.get(spark.sparkContext().hadoopConfiguration());
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
