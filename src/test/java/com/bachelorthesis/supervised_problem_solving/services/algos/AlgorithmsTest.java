package com.bachelorthesis.supervised_problem_solving.services.algos;

import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.vo.ChartDataVO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlgorithmsTest {

    private final static List<ChartDataVO> CHART_DATA_VOS_100 = new LinkedList<>();
    private final static List<ChartDataVO> CHART_DATA_VOS_BOUNCING = new LinkedList<>();
    private final static List<ChartDataVO> CHART_DATA_VOS_BOUNCING_INVERSED = new LinkedList<>();
    private final static int DELTA = 5;
    private final static int ELEMENTS_TO_BE_CREATED = 210;

    @BeforeAll
    public static void setup() {
        for (int start = 0; start < ELEMENTS_TO_BE_CREATED; start++) {

            final ChartDataVO chartDataVO100 = new ChartDataVO();
            final ChartDataVO chartDataVObouncing = new ChartDataVO();
            final ChartDataVO chartDataVObouncingInversed = new ChartDataVO();

            if (start % 2 == 0) {
                chartDataVObouncingInversed.setClose(0);
                chartDataVObouncing.setClose(50);
            } else {
                chartDataVObouncingInversed.setClose(50);
                chartDataVObouncing.setClose(0);
            }
            chartDataVO100.setClose(100.0);

            setDate(start, chartDataVO100, chartDataVObouncing, chartDataVObouncingInversed);

            CHART_DATA_VOS_BOUNCING_INVERSED.add(chartDataVObouncingInversed);
            CHART_DATA_VOS_BOUNCING.add(chartDataVObouncing);
            CHART_DATA_VOS_100.add(chartDataVO100);
        }
    }

    private static void setDate(int start, ChartDataVO chartDataVO100, ChartDataVO chartDataVObouncing, ChartDataVO chartDataVObouncingInversed) {
        final LocalDateTime localDateTime = LocalDateTime.now().minusDays(ELEMENTS_TO_BE_CREATED - start);
        chartDataVO100.setLocalDateTime(localDateTime);
        chartDataVObouncingInversed.setLocalDateTime(localDateTime);
        chartDataVObouncing.setLocalDateTime(localDateTime);
    }


    @Test
    void getReturns() {
        final double expectedReturnsFix = 0.0;
        final double expectedReturnsBouncing = -50;
        final double expectedReturnsBouncingInversed = 50;

        double accumulatedFixReturns = 0;
        double accumulatedBouncingReturns = 0;
        double accumulatedBouncingInversedReturns = 0;

        final List<Double> bouncingReturns = Algorithms.getReturns(CHART_DATA_VOS_BOUNCING, DELTA);
        final List<Double> bouncingReturnsInversed = Algorithms.getReturns(CHART_DATA_VOS_BOUNCING_INVERSED, DELTA);
        final List<Double> fixReturns = Algorithms.getReturns(CHART_DATA_VOS_100, DELTA);

        for (int counter = 0; counter < ELEMENTS_TO_BE_CREATED - DELTA; counter++) {
            accumulatedFixReturns += fixReturns.get(counter);
            accumulatedBouncingReturns += bouncingReturns.get(counter);
            accumulatedBouncingInversedReturns += bouncingReturnsInversed.get(counter);
        }

        assertEquals(ELEMENTS_TO_BE_CREATED - DELTA, bouncingReturns.size());
        assertEquals(ELEMENTS_TO_BE_CREATED - DELTA, bouncingReturnsInversed.size());
        assertEquals(ELEMENTS_TO_BE_CREATED - DELTA, fixReturns.size());
        assertEquals(expectedReturnsFix, accumulatedFixReturns);
        assertEquals(expectedReturnsBouncing, accumulatedBouncingReturns);
        assertEquals(accumulatedBouncingInversedReturns, expectedReturnsBouncingInversed);
    }


    @Test
    void getRsi() {
        assertEquals(Algorithms.getRsi(CHART_DATA_VOS_BOUNCING, 7),
                Algorithms.getRsi(CHART_DATA_VOS_BOUNCING_INVERSED, 7));
        assertEquals(Algorithms.getRsi(CHART_DATA_VOS_BOUNCING, 7),
                Algorithms.getRsi(CHART_DATA_VOS_100, 7));
    }

    @Test
    void getMac() {
    }

    @Test
    void getIndicatorValues() {
    }
}
