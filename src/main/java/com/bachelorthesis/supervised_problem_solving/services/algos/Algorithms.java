package com.bachelorthesis.supervised_problem_solving.services.algos;

import com.bachelorthesis.supervised_problem_solving.configuration.RuntimeDataStorage;
import com.bachelorthesis.supervised_problem_solving.enums.Indicators;
import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.vo.ChartDataVO;
import org.apache.commons.lang3.ArrayUtils;
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
        final List<Double> resultList = new LinkedList<>();

        for (int start = 0; start < chartDataVOS.size() - delta; start++) {
            resultList.add(chartDataVOS.get(delta + start).getClose() - chartDataVOS.get(start).getClose());
        }

        return resultList.subList(resultList.size() - RuntimeDataStorage.getMatrixRowLength(), resultList.size());
    }

    public static double[] toPrimitive(List<Double> doubleList) {
        Double[] ds = doubleList.toArray(new Double[doubleList.size()]);
        return ArrayUtils.toPrimitive(ds);
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
        return setupList(chartDataVOList, rsiIndicator);
    }

    private static List<Double> setupList(List<ChartDataVO> chartDataVOList, RSIIndicator rsiIndicator) {
        final List<Double> indicatorResults = new ArrayList<>();
        for (int i = 0; i < chartDataVOList.size(); i++) {
            indicatorResults.add(rsiIndicator.getValue(i).doubleValue());
        }
        return indicatorResults.subList(indicatorResults.size() - RuntimeDataStorage.getMatrixRowLength(), indicatorResults.size());
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
        return indicatorResults.subList(indicatorResults.size() - RuntimeDataStorage.getMatrixRowLength(), indicatorResults.size());
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
                    break;
                case MACD:
                    technicalIndicatorResults.add(Algorithms.getMac(chartDataVOList));
                    break;
            }
        }
        return technicalIndicatorResults;
    }
}
