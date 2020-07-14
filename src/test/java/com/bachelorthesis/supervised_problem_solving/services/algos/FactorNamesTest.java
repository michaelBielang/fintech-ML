package com.bachelorthesis.supervised_problem_solving.services.algos;

import com.bachelorthesis.supervised_problem_solving.enums.Indicators;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FactorNamesTest {

    // delta between bars
    private final int[] barDelta = new int[]{5, 10, 15, 20, 25, 30, 60};

    private static final int INDICATOR_DELTA = 21;

    @Test
    public void getFactorNames() {
        final List<Indicators> indicators = List.of(Indicators.MACD, Indicators.RSI);

        final List<String> factorNames = FactorNames.getFactorNames(indicators, barDelta);
        assertEquals(11, factorNames.size());
    }

    @Test
    public void getRowsToIgnore() {
        assertEquals(INDICATOR_DELTA, FactorNames.getRowsToIgnore());
    }

    @Test
    public void getIndicatorDeltas() {
        assertEquals(3, FactorNames.getIndicatorDeltas(Indicators.RSI).size());
    }

}
