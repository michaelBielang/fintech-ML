package com.bachelorthesis.supervised_problem_solving.dl4jApp;

import com.bachelorthesis.supervised_problem_solving.dl4jApp.vo.ResultSet;
import com.bachelorthesis.supervised_problem_solving.dl4jApp.vo.TechnicalIndicators;
import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.vo.ChartDataVO;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Dl4jLinearRegressionService {

    private final List<ResultSet> resultSetList = new LinkedList<>();

    // nMinute future return
    private final int tradingFrequency = 60;

    // vector of nMinute returns that we want to use as the
    private final int[] nMinReturns = new int[]{5, 10, 15, 20, 25, 30, 60};

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
    public void calculateSignals(final List<ChartDataVO> chartDataVOList, final List<TechnicalIndicators> technicalIndicatorsList,
                                 final boolean downSample) {
        // chartDataVO; date, min, bid, ask
        // nMinReturns; see field
        // other Signals:
        /* fnHand, params, field
        'macd'	    []	'Mid'
        'rsindex'	5	'Mid'
        'rsindex'	10	'Mid'
        'rsindex'	15	'Mid'
        'rsindex'	20	'Mid'
        'rsindex'	25	'Mid'
        'rsindex'	30	'Mid'
        'rsindex'	60	'Mid'*/
        //minFutReturn --> tradingFrequency


        final List<String> factorNames = Arrays.stream(nMinReturns)
                .mapToObj(entry -> "Return_" + entry)
                .collect(Collectors.toList());
        factorNames.addAll((createSignalTimeFrameList(technicalIndicatorsList)));

        final List<Integer> allFactors = factorNames.stream()
                .map(entry -> technicalIndicatorsList.size())
                .collect(Collectors.toList());

        final int factorCount = factorNames.size();

    }


    public List<String> createSignalTimeFrameList(List<TechnicalIndicators> technicalIndicatorsList) {
        return technicalIndicatorsList.stream()
                .map(entry ->
                {
                    if (entry.getTimeFrame().isPresent()) {
                        return entry.getSignalName() + "_" + entry.getTimeFrame().get();
                    }
                    return entry.getSignalName();
                })
                .collect(Collectors.toList());
    }

}
