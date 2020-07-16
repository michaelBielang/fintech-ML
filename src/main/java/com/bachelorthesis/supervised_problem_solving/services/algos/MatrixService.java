package com.bachelorthesis.supervised_problem_solving.services.algos;

import com.bachelorthesis.supervised_problem_solving.configuration.RuntimeDataStorage;
import com.bachelorthesis.supervised_problem_solving.enums.Indicators;
import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.vo.ChartDataVO;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.List;

public class MatrixService {

    public static INDArray fillMatrixWithPredictors(List<ChartDataVO> chartDataVOList, List<String> factorNames,
                                                    final int[] barDeltas, final List<Indicators> technicalIndicatorsList) {

        final INDArray factorMatrix = createEmptyFactorMatrix(factorNames);
        fillMatrixWithResults(factorNames, technicalIndicatorsList, factorMatrix, chartDataVOList, barDeltas);

        return factorMatrix;
    }

    private static void fillMatrixWithResults(List<String> factorNames, final List<Indicators> technicalIndicatorsList,
                                              INDArray factorMatrix, final List<ChartDataVO> chartDataVOList, int[] barDeltas) {

        final int expectedRows = RuntimeDataStorage.getMatrixRowLength();
        for (int index = 0; index < factorNames.size(); index++) {

            if (index < barDeltas.length) {

                List<Double> returns = Algorithms.getReturns(chartDataVOList, barDeltas[index]);
                final int entriesToRemove = returns.size() - expectedRows;
                returns = returns.subList(entriesToRemove, returns.size());

                final INDArray indArray = Nd4j.create(returns);

                factorMatrix.getColumn(index).addi(indArray);
            } else {
                final List<List<Double>> indicatorValues = Algorithms.getIndicatorValues(chartDataVOList, technicalIndicatorsList);
                final int entriesToRemove = indicatorValues.get(index - barDeltas.length).size() - expectedRows;
                final INDArray indArray = Nd4j.create(indicatorValues.get(index - barDeltas.length)
                        .subList(entriesToRemove, indicatorValues.get(index - barDeltas.length).size()));

                factorMatrix.getColumn(index).addi(indArray);
            }
        }
    }

    private static INDArray createEmptyFactorMatrix(List<String> factorNames) {
        final int columns = factorNames.size();
        final int rows = RuntimeDataStorage.getMatrixRowLength();

        return Nd4j.zeros(rows, columns);
    }
}
