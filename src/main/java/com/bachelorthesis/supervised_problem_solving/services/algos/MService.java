package com.bachelorthesis.supervised_problem_solving.services.algos;

import com.bachelorthesis.supervised_problem_solving.enums.Indicators;
import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.vo.ChartDataVO;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.List;

import static com.bachelorthesis.supervised_problem_solving.services.algos.FactorNames.getRowsToIgnore;

public class MService {

    private static int rows;
    public static INDArray getFilledMatrix(List<ChartDataVO> chartDataVOList, List<String> factorNames,
                                           final int[] barDeltas, final List<Indicators> technicalIndicatorsList) {
        MService.rows = chartDataVOList.size() - 1;

        final INDArray factorMatrix = createEmptyFactorMatrix(factorNames);
        fillMatrixWithResults(factorNames, technicalIndicatorsList, factorMatrix, chartDataVOList, barDeltas);

        return factorMatrix;
    }

    private static void fillMatrixWithResults(List<String> factorNames, final List<Indicators> technicalIndicatorsList,
                                              INDArray factorMatrix, final List<ChartDataVO> chartDataVOList, int[] barDeltas) {

        for (int index = 0; index < factorNames.size(); index++) {

            if (index < barDeltas.length) {

                final int returnsToIgnore = getRowsToIgnore() - barDeltas[index];
                final List<Double> returns = Algorithms.getReturns(chartDataVOList, barDeltas[index])
                        .subList(returnsToIgnore, rows- barDeltas[index]);

                final INDArray indArray = Nd4j.create(returns);

                factorMatrix.getColumn(index).addi(indArray);
            } else {
                final List<List<Double>> indicatorValues = Algorithms.getIndicatorValues(chartDataVOList, technicalIndicatorsList);
                final INDArray indArray = Nd4j.create(indicatorValues.get(index - barDeltas.length).subList(getRowsToIgnore(), rows));

                factorMatrix.getColumn(index).addi(indArray);
            }
        }
    }

    private static INDArray createEmptyFactorMatrix(List<String> factorNames) {
        final int columns = factorNames.size();
        final int rows = MService.rows - getRowsToIgnore();

        return Nd4j.zeros(rows, columns);
    }
}
