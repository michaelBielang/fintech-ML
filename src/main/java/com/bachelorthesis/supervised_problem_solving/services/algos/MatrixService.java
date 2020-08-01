package com.bachelorthesis.supervised_problem_solving.services.algos;

import com.bachelorthesis.supervised_problem_solving.configuration.RuntimeDataStorage;
import com.bachelorthesis.supervised_problem_solving.enums.Indicators;
import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.vo.ChartDataVO;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.LinkedList;
import java.util.List;

public class MatrixService {

    public static INDArray fillMatrixWithPredictors(final List<ChartDataVO> chartDataVOList, final List<String> factorNames,
                                                    final int[] barDeltas, final List<Indicators> technicalIndicatorsList) {

        final INDArray factorMatrix = createEmptyFactorMatrix(factorNames);
        fillMatrixWithResults(factorNames, technicalIndicatorsList, factorMatrix, chartDataVOList, barDeltas);

        return factorMatrix;
    }

    private static void fillMatrixWithResults(final List<String> factorNames, final List<Indicators> technicalIndicatorsList,
                                              final INDArray factorMatrix, final List<ChartDataVO> chartDataVOList,
                                              final int[] barDeltas) {
        for (int index = 0; index < factorNames.size(); index++) {

            if (index < barDeltas.length) {
                // add returns
                addReturnsToMatrix(factorMatrix.getColumn(index), Algorithms.getReturns(chartDataVOList, barDeltas[index]));
            } else {
                // add indicators
                final List<List<Double>> indicatorValues = Algorithms.getIndicatorValues(chartDataVOList, technicalIndicatorsList);
                int innerIndex = index;

                for (List<Double> indicatorValue : indicatorValues) {
                    final INDArray indArray = Nd4j.create(indicatorValue);
                    factorMatrix.getColumn(innerIndex++).addi(indArray);
                }
                break;
            }
        }
    }

    private static void addReturnsToMatrix(final INDArray column, final List<Double> returns) {
        final INDArray indArray = Nd4j.create(returns);

        column.addi(indArray);
    }

    private static INDArray createEmptyFactorMatrix(final List<String> factorNames) {
        final int columns = factorNames.size();
        final int rows = RuntimeDataStorage.getMatrixRowLength();

        return Nd4j.zeros(rows, columns);
    }

    public static List<Row> getRowList(final List<ChartDataVO> chartDataVOList, final List<String> factorNames,
                                       final int[] barDeltas, final List<Indicators> technicalIndicatorsList) {
        final List<Row> rowList = new LinkedList<>();
        final INDArray indArray = fillMatrixWithPredictors(chartDataVOList, factorNames, barDeltas, technicalIndicatorsList);

        for (int rowIndex = 0; rowIndex < indArray.rows(); rowIndex++) {
            final Double[] doubleVec = ArrayUtils.toObject(indArray.getRow(rowIndex).toDoubleVector());
            final Row row = RowFactory.create((Object[]) doubleVec);
            rowList.add(row);
        }
        return rowList;
    }
}
