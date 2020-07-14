package com.bachelorthesis.supervised_problem_solving.dl4jApp;

import com.bachelorthesis.supervised_problem_solving.enums.Indicators;
import com.bachelorthesis.supervised_problem_solving.services.algos.MatrixService;
import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.vo.ChartDataVO;
import lombok.Data;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.util.List;

import static com.bachelorthesis.supervised_problem_solving.services.algos.FactorNames.getFactorNames;

@Data
public class Dl4jLinearRegressionService {

/*
    // delta between bars
    private final int[] barDelta = new int[]{5, 10, 15, 20, 25, 30, 60};
*/

    // delta between bars
    private final int[] barDelta = new int[]{5, 10};

    /**
     * price - timetable of prices. I MAKE THE ASSUMPTION THAT THIS VARIABLE
     * IS OF ONE MINUTE BAR
     * predictor
     * otherSignals - function handles referring to other sigals
     *
     * @param chartDataVOList
     * @param technicalIndicatorsList function handles referring to other signals
     */
    public void calculateSignals(final List<ChartDataVO> chartDataVOList, final List<Indicators> technicalIndicatorsList) {

        final List<String> factorNames = getFactorNames(technicalIndicatorsList, barDelta);

        final INDArray factorMatrix = MatrixService.getFilledMatrix(chartDataVOList, factorNames, barDelta, technicalIndicatorsList);
        System.out.println(factorNames);
        System.out.println(factorMatrix);
    }

}
