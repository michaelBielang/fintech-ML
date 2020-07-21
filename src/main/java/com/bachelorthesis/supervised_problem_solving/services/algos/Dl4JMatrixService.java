package com.bachelorthesis.supervised_problem_solving.services.algos;

import com.bachelorthesis.supervised_problem_solving.configuration.RuntimeDataStorage;
import com.bachelorthesis.supervised_problem_solving.enums.Indicators;
import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.vo.ChartDataVO;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.List;

public class Dl4JMatrixService {

    public static INDArray fillMatrixWithPredictors(List<ChartDataVO> chartDataVOList, List<String> factorNames,
                                                    final int[] barDeltas, final List<Indicators> technicalIndicatorsList) {

        final INDArray factorMatrix = createEmptyFactorMatrix(factorNames);
        fillMatrixWithResults(factorNames, technicalIndicatorsList, factorMatrix, chartDataVOList, barDeltas);

        return factorMatrix;
    }

    private static void fillMatrixWithResults(List<String> factorNames, final List<Indicators> technicalIndicatorsList,
                                              INDArray factorMatrix, final List<ChartDataVO> chartDataVOList, int[] barDeltas) {
        for (int index = 0; index < factorNames.size(); index++) {

            //returns
            if (index < barDeltas.length) {
                addReturnsToMatrix(factorMatrix.getColumn(index), Algorithms.getReturns(chartDataVOList, barDeltas[index]));
            } else {
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

    private static void addReturnsToMatrix(INDArray column, List<Double> returns1) {
        List<Double> returns = returns1;

        final INDArray indArray = Nd4j.create(returns);

        column.addi(indArray);
    }

    private static INDArray createEmptyFactorMatrix(List<String> factorNames) {
        final int columns = factorNames.size();
        final int rows = RuntimeDataStorage.getMatrixRowLength();

        return Nd4j.zeros(rows, columns);
    }
}
