package com.bachelorthesis.supervised_problem_solving.services.algos;

import com.bachelorthesis.supervised_problem_solving.configuration.RuntimeDataStorage;
import com.bachelorthesis.supervised_problem_solving.enums.Indicators;
import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.vo.ChartDataVO;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.util.*;

public class ApacheMatrixService {

    private static final Map<Integer, double[]> MATRIX = new LinkedHashMap<>();

    public static List<Row> getRowList(List<ChartDataVO> chartDataVOList, List<String> factorNames,
                                       final int[] barDeltas, final List<Indicators> technicalIndicatorsList) {
        final List<Row> rowList = new LinkedList<>();
        final INDArray indArray = Dl4JMatrixService.fillMatrixWithPredictors(chartDataVOList, factorNames, barDeltas, technicalIndicatorsList);

        for (int rowIndex = 0; rowIndex < indArray.rows(); rowIndex++) {
            final Double[] doubleVec = ArrayUtils.toObject(indArray.getRow(rowIndex).toDoubleVector());
            final Row row = RowFactory.create((Object[]) doubleVec);
            rowList.add(row);
        }
        return rowList;
    }

    public static Map<Integer, double[]> fillMatrixWithPredictors(List<ChartDataVO> chartDataVOList, List<String> factorNames,
                                                                  final int[] barDeltas, final List<Indicators> technicalIndicatorsList) {
        MATRIX.clear();
        fillMatrixWithResults(factorNames, technicalIndicatorsList, chartDataVOList, barDeltas);

        return MATRIX;
    }

    private static void fillMatrixWithResults(List<String> factorNames, final List<Indicators> technicalIndicatorsList,
                                              final List<ChartDataVO> chartDataVOList, int[] barDeltas) {

        final int expectedRows = RuntimeDataStorage.getMatrixRowLength();
        for (int index = 0; index < factorNames.size(); index++) {

            if (index < barDeltas.length) {

                List<Double> returns = Algorithms.getReturns(chartDataVOList, barDeltas[index]);
                final int entriesToRemove = returns.size() - expectedRows;
                returns = returns.subList(entriesToRemove, returns.size());

                Double[] ds = returns.toArray(new Double[returns.size()]);
                double[] returnsArray = ArrayUtils.toPrimitive(ds);

                MATRIX.put(index, returnsArray);
            } else {
                final List<List<Double>> indicatorValues = Algorithms.getIndicatorValues(chartDataVOList, technicalIndicatorsList);
                final int entriesToRemove = indicatorValues.get(index - barDeltas.length).size() - expectedRows;

                List<Double> resultList = indicatorValues.get(index - barDeltas.length)
                        .subList(entriesToRemove, indicatorValues.get(index - barDeltas.length).size());

                Double[] ds = resultList.toArray(new Double[resultList.size()]);
                double[] returnsArray = ArrayUtils.toPrimitive(ds);

                MATRIX.put(index, returnsArray);
            }
        }
    }
}
