package com.bachelorthesis.supervised_problem_solving.services.algos;

import com.bachelorthesis.supervised_problem_solving.enums.Indicators;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class FactorNames {


    /**
     * @param technicalIndicatorsList
     * @param barDelta
     * @return list with returns, size barDelta.length + multiple values
     */
    public static List<String> getFactorNames(List<Indicators> technicalIndicatorsList, final int[] barDelta) {

        // add returns
        final List<String> factorNames = Arrays.stream(barDelta)
                .mapToObj(entry -> "Return_" + entry)
                .collect(Collectors.toList());

        // add indicators
        factorNames.addAll(createIndicatorBarDeltaList(technicalIndicatorsList));

        return factorNames;
    }


    private static List<String> createIndicatorBarDeltaList(List<Indicators> technicalIndicatorsList) {
        final List<String> signalList = new LinkedList<>();
        technicalIndicatorsList.forEach(indicator -> {
            switch (indicator) {
                case RSI:
                    signalList.addAll(getIndicatorDeltas(indicator).stream()
                            .map(barDelta -> indicator + "_" + barDelta)
                            .collect(Collectors.toList()));
                    break;
                case MACD:
                    signalList.add("MACD_12_26");
                    break;
                default:
                    break;
            }
        });
        return signalList;
    }

    public static List<Integer> getIndicatorDeltas(final Indicators indicators) {
        switch (indicators) {
            case RSI:
                return List.of(7, 14, 21);
            case MACD:
                return List.of(12, 26);
            default:
                return Collections.emptyList();
        }
    }

    public static int getRowsToIgnore() {
        return Collections.max(getIndicatorDeltas(Indicators.RSI));
    }
}
