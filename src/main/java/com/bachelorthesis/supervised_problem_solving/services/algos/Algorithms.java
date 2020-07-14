package com.bachelorthesis.supervised_problem_solving.services.algos;

import com.bachelorthesis.supervised_problem_solving.enums.Indicators;
import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.vo.ChartDataVO;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.indicators.MACDIndicator;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Algorithms {

    private static BarSeries series;

    public static List<Double> getReturns(final List<ChartDataVO> chartDataVOS, final int delta) {
        final List<Double> resultSet = new LinkedList<>();

        for (int start = 0; start < chartDataVOS.size() - delta; start++) {
            resultSet.add(chartDataVOS.get(delta + start).getClose() - chartDataVOS.get(start).getClose());
        }
        return resultSet;
    }

    /**
     * > 70 overbought
     * < 30 oversold
     * The RSI Indicator measures the ratio of moves higher to moves lower in a specific period.
     * Wilder preferred a period of 14 (minutes, hours, days, etc.). up-moves to down-moves.
     * The RSI index is expressed in a range of 0-100.
     *
     * @param chartDataVOList
     * @param bars            0 = default || > 5; numbers of bars to consider
     * @return
     */
    public static List<Double> getRsi(final List<ChartDataVO> chartDataVOList, int bars) {
        if (bars < 5) {
            bars = 5;
        }
        initBars(chartDataVOList);

        RSIIndicator rsiIndicator = new RSIIndicator(new ClosePriceIndicator(series), bars);
        final List<Double> indicatorResults = new ArrayList<>();
        for (int i = 0; i < chartDataVOList.size(); i++) {
            indicatorResults.add(rsiIndicator.getValue(i).doubleValue());
        }
        return indicatorResults;
    }

    /**
     * Default short bar count: 12 and long bar count: 26
     *
     * @param chartDataVOList
     * @return
     */
    public static List<Double> getMac(final List<ChartDataVO> chartDataVOList) {
        initBars(chartDataVOList);

        MACDIndicator rsiIndicator = new MACDIndicator(new ClosePriceIndicator(series));
        final List<Double> indicatorResults = new ArrayList<>();
        for (int i = 0; i < chartDataVOList.size(); i++) {
            indicatorResults.add(rsiIndicator.getValue(i).doubleValue());
        }
        return indicatorResults;
    }

    private static void initBars(List<ChartDataVO> chartDataVOList) {
        if (series == null) {
            series = new BaseBarSeriesBuilder().withName("bars").build();
            series.setMaximumBarCount(chartDataVOList.size() + 1);

            for (ChartDataVO chartDataVO : chartDataVOList) {
                series.addBar(ZonedDateTime.of(chartDataVO.getLocalDateTime(), ZoneId.of("UTC")), chartDataVO.getOpen(),
                        chartDataVO.getHigh(), chartDataVO.getLow(), chartDataVO.getClose(), chartDataVO.getQuoteVolume());
            }
        }
    }

    public static List<List<Double>> getIndicatorValues(List<ChartDataVO> chartDataVOList, List<Indicators> technicalIndicatorsList) {
        final List<List<Double>> technicalIndicatorResults = new LinkedList<>();

        for (Indicators indicator : technicalIndicatorsList) {
            switch (indicator) {
                case RSI:
                    FactorNames.getIndicatorDeltas(indicator)
                            .forEach(delta -> technicalIndicatorResults.add(Algorithms.getRsi(chartDataVOList, delta)));
                case MACD:
                    technicalIndicatorResults.add(Algorithms.getMac(chartDataVOList));
            }
        }
        return technicalIndicatorResults;
    }
}
