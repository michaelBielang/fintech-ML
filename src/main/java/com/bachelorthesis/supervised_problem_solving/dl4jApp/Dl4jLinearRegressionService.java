package com.bachelorthesis.supervised_problem_solving.dl4jApp;

import com.bachelorthesis.supervised_problem_solving.dl4jApp.vo.ResultSet;
import com.bachelorthesis.supervised_problem_solving.enums.Indicators;
import com.bachelorthesis.supervised_problem_solving.services.algos.FactorNames;
import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.enums.Periods;
import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.vo.ChartDataVO;
import lombok.Data;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.util.LinkedList;
import java.util.List;

import static com.bachelorthesis.supervised_problem_solving.services.algos.Algorithms.getIndicatorValues;
import static com.bachelorthesis.supervised_problem_solving.services.algos.Algorithms.getReturns;
import static com.bachelorthesis.supervised_problem_solving.services.algos.FactorNames.getFactorNames;

@Data
public class Dl4jLinearRegressionService {

    private final List<ResultSet> resultSetList = new LinkedList<>();
    private final List<Periods> periods;

    // delta between bars
    private final int[] barDelta = new int[]{5, 10, 15, 20, 25, 30, 60};

    final int rowsToIgnore = FactorNames.getIndicatorDeltas(Indicators.RSI)
            .get(FactorNames.getIndicatorDeltas(Indicators.RSI).size() - 1);

    final int lengthOfRelevantRows;

    /**
     * price - timetable of prices. I MAKE THE ASSUMPTION THAT THIS VARIABLE
     * IS OF ONE MINUTE BAR
     * predictor
     * otherSignals - function handles referring to other sigals
     *
     * @param chartDataVOList
     * @param technicalIndicatorsList function handles referring to other signals
     * @param downSample
     */
    public void calculateSignals(final List<ChartDataVO> chartDataVOList, final List<Indicators> technicalIndicatorsList,
                                 final boolean downSample) {

        final List<String> factorNames = getFactorNames(technicalIndicatorsList, barDelta);

        INDArray factorMatrix = createEmptyFactorMatrix(chartDataVOList, factorNames);

        fillMatrixWithResults(factorNames, technicalIndicatorsList, factorMatrix, chartDataVOList);
    }

    private void fillMatrixWithResults(List<String> factorNames, final List<Indicators> technicalIndicatorsList,
                                       INDArray factorMatrix, final List<ChartDataVO> chartDataVOList) {
        for (int index = 0; index < factorNames.size(); index++) {

            if (index < barDelta.length) {

                final List<Double> returns = getReturns(chartDataVOList, barDelta[index])
                        .subList(rowsToIgnore, lengthOfRelevantRows);

                final INDArray indArray = Nd4j.create(returns);
                factorMatrix.getColumn(index).addi(indArray);
            } else {
                final List<List<Double>> indicatorValues = getIndicatorValues(chartDataVOList, technicalIndicatorsList);
                final INDArray indArray = Nd4j.create(indicatorValues.get(index - barDelta.length).subList(rowsToIgnore, lengthOfRelevantRows));
                factorMatrix.getColumn(index).addi(indArray);
            }
        }
    }

    private INDArray createEmptyFactorMatrix(List<ChartDataVO> chartDataVOList, List<String> factorNames) {
        final int columns = factorNames.size();
        final int rows = lengthOfRelevantRows - barDelta.length;

        return Nd4j.zeros(rows, columns);
    }
}
