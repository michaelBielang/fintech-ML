package com.bachelorthesis.supervised_problem_solving.services.algos;

import com.bachelorthesis.supervised_problem_solving.configuration.RuntimeDataStorage;
import com.bachelorthesis.supervised_problem_solving.enums.Indicators;
import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.vo.ChartDataVO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static com.bachelorthesis.supervised_problem_solving.enums.Indicators.MACD;
import static com.bachelorthesis.supervised_problem_solving.enums.Indicators.RSI;
import static com.bachelorthesis.supervised_problem_solving.services.algos.FactorNames.getIndicatorDeltas;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AlgorithmsTest {

    private final static List<ChartDataVO> CHART_DATA_VOS_100 = new LinkedList<>();
    private final static List<ChartDataVO> CHART_DATA_VOS_BOUNCING = new LinkedList<>();
    private final static List<ChartDataVO> CHART_DATA_VOS_BOUNCING_INVERSE = new LinkedList<>();
    private final static int DELTA = 5;
    private final static int ELEMENTS_TO_BE_CREATED = 210;

    @BeforeAll
    public static void setup() {
        RuntimeDataStorage.setMatrixRowLength(ELEMENTS_TO_BE_CREATED - DELTA);
        for (int start = 0; start < ELEMENTS_TO_BE_CREATED; start++) {

            final ChartDataVO chartDataVO100 = new ChartDataVO();
            final ChartDataVO chartDataVObouncing = new ChartDataVO();
            final ChartDataVO chartDataVObouncingInverse = new ChartDataVO();

            if (start % 2 == 0) {
                chartDataVObouncingInverse.setClose(0);
                chartDataVObouncing.setClose(50);
            } else {
                chartDataVObouncingInverse.setClose(50);
                chartDataVObouncing.setClose(0);
            }
            chartDataVO100.setClose(100.0);

            setDate(start, chartDataVO100, chartDataVObouncing, chartDataVObouncingInverse);

            CHART_DATA_VOS_BOUNCING_INVERSE.add(chartDataVObouncingInverse);
            CHART_DATA_VOS_BOUNCING.add(chartDataVObouncing);
            CHART_DATA_VOS_100.add(chartDataVO100);
        }
    }

    private static void setDate(final int start, final ChartDataVO chartDataVO100, final ChartDataVO chartDataVObouncing,
                                final ChartDataVO chartDataVOListBouncingInverse) {
        final LocalDateTime localDateTime = LocalDateTime.now().minusDays(ELEMENTS_TO_BE_CREATED - start);
        chartDataVO100.setLocalDateTime(localDateTime);
        chartDataVOListBouncingInverse.setLocalDateTime(localDateTime);
        chartDataVObouncing.setLocalDateTime(localDateTime);
    }


    @Test
    void getReturns() {
        final double expectedReturnsFix = 0.0;
        final double expectedReturnsBouncing = -50;
        final double expectedReturnsBouncingInverse = 50;

        double accumulatedFixReturns = 0;
        double accumulatedBouncingReturns = 0;
        double accumulatedBouncingInversedReturns = 0;

        final List<Double> bouncingReturns = Algorithms.getReturns(CHART_DATA_VOS_BOUNCING, DELTA);
        final List<Double> bouncingReturnsInverse = Algorithms.getReturns(CHART_DATA_VOS_BOUNCING_INVERSE, DELTA);
        final List<Double> fixReturns = Algorithms.getReturns(CHART_DATA_VOS_100, DELTA);

        for (int counter = 0; counter < ELEMENTS_TO_BE_CREATED - DELTA; counter++) {
            accumulatedFixReturns += fixReturns.get(counter);
            accumulatedBouncingReturns += bouncingReturns.get(counter);
            accumulatedBouncingInversedReturns += bouncingReturnsInverse.get(counter);
        }

        assertEquals(ELEMENTS_TO_BE_CREATED - DELTA, bouncingReturns.size());
        assertEquals(ELEMENTS_TO_BE_CREATED - DELTA, bouncingReturnsInverse.size());
        assertEquals(ELEMENTS_TO_BE_CREATED - DELTA, fixReturns.size());
        assertEquals(expectedReturnsFix, accumulatedFixReturns);
        assertEquals(expectedReturnsBouncing, accumulatedBouncingReturns);
        assertEquals(accumulatedBouncingInversedReturns, expectedReturnsBouncingInverse);
    }


    @Test
    void getRsi() {
        assertEquals(Algorithms.getRsi(CHART_DATA_VOS_BOUNCING, 7),
                Algorithms.getRsi(CHART_DATA_VOS_BOUNCING_INVERSE, 7));
        assertEquals(Algorithms.getRsi(CHART_DATA_VOS_BOUNCING, 7),
                Algorithms.getRsi(CHART_DATA_VOS_100, 7));
    }

    @Test
    void getMac() {
        assertEquals(Algorithms.getMac(CHART_DATA_VOS_BOUNCING), Algorithms.getMac(CHART_DATA_VOS_BOUNCING_INVERSE));
        assertEquals(Algorithms.getMac(CHART_DATA_VOS_BOUNCING), Algorithms.getMac(CHART_DATA_VOS_100));
    }

    @Test
    void getIndicatorValues() {
        final List<Indicators> indicatorsList = List.of(RSI, MACD);
        final List<List<Double>> indicatorValues = Algorithms.getIndicatorValues(CHART_DATA_VOS_100, indicatorsList);

        indicatorsList.forEach(indicator -> {
            final int[] index = new int[]{0};
            switch (indicator) {
                case RSI:
                    getIndicatorDeltas(indicator).forEach(indicatorValue -> {
                        assertEquals(Algorithms.getRsi(CHART_DATA_VOS_100, indicatorValue), indicatorValues.get(index[0]++));
                    });
                    break;
                case MACD:
                    getIndicatorDeltas(indicator).forEach(indicatorValue -> {
                        assertEquals(Algorithms.getMac(CHART_DATA_VOS_100), indicatorValues.get(index[0]++));
                    });
                    break;
            }
        });
    }
}
