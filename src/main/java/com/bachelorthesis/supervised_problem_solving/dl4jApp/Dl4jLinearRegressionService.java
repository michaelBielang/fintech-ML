package com.bachelorthesis.supervised_problem_solving.dl4jApp;

import com.bachelorthesis.supervised_problem_solving.enums.Indicators;
import com.bachelorthesis.supervised_problem_solving.dl4jApp.vo.ResultSet;
import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.enums.Periods;
import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.vo.ChartDataVO;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

import static com.bachelorthesis.supervised_problem_solving.services.algos.FactorNames.getFactorNames;

@Data
public class Dl4jLinearRegressionService {

    private final List<ResultSet> resultSetList = new LinkedList<>();
    private final List<Periods> periods;

    // delta between bars
    private final int[] barDelta = new int[]{5, 10, 15, 20, 25, 30, 60};

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


        final List<String> factorNames = getFactorNames(technicalIndicatorsList, barDelta);

    }



}
