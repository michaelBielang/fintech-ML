package com.bachelorthesis.supervised_problem_solving.configuration;

import com.bachelorthesis.supervised_problem_solving.enums.Indicators;
import com.bachelorthesis.supervised_problem_solving.services.algos.FactorNames;
import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.vo.ChartDataVO;

import java.util.Collections;
import java.util.List;

public class RuntimeDataStorage {


    private static int matrixRowLength;

    public void findAndSetMaximumMatrixRows(List<ChartDataVO> chartDataVOList, List<Indicators> technicalIndicatorsList, int[] barDeltas,
                                            final int tradingFrequency) {
        int maximum = 0;
        for (Indicators indicator : technicalIndicatorsList) {
            int maxFromIndicator = Collections.max(FactorNames.getIndicatorDeltas(indicator));
            if (maxFromIndicator > maximum) {
                maximum = maxFromIndicator;
            }
        }
        for (int delta : barDeltas) {
            if (delta > maximum) {
                maximum = delta;
            }
        }
        RuntimeDataStorage.matrixRowLength = chartDataVOList.size() - Math.max(maximum, tradingFrequency);
    }

    public void findAndSetMaximumMatrixRows(List<ChartDataVO> pastData, List<ChartDataVO> testData, List<Indicators> technicalIndicatorsList, int[] barDeltas,
                                            final int tradingFrequency) {
        int maximum = 0;
        for (Indicators indicator : technicalIndicatorsList) {
            int maxFromIndicator = Collections.max(FactorNames.getIndicatorDeltas(indicator));
            if (maxFromIndicator > maximum) {
                maximum = maxFromIndicator;
            }
        }
        for (int delta : barDeltas) {
            if (delta > maximum) {
                maximum = delta;
            }
        }
        final int shortestList = Math.min(pastData.size(), testData.size());
        RuntimeDataStorage.matrixRowLength = shortestList - Math.max(maximum, tradingFrequency);
    }

    public static int getMatrixRowLength() {
        return matrixRowLength;
    }

    public static void setMatrixRowLength(int matrixRowLength) {
        RuntimeDataStorage.matrixRowLength = matrixRowLength;
    }
}
