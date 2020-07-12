package com.bachelorthesis.supervised_problem_solving.services.algos;

import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.vo.ChartDataVO;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.indicators.MACDIndicator;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.Num;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Algorithms {

    private static BarSeries series;

    public static LinkedHashMap<ChartDataVO, Double> getReturns(final List<ChartDataVO> chartDataVOS, final int delta) {
        final LinkedHashMap<ChartDataVO, Double> resultSet = new LinkedHashMap<>();

        for (int start = 0; start < chartDataVOS.size() - delta; start++) {
            resultSet.put(chartDataVOS.get(delta + start),
                    chartDataVOS.get(delta + start).getClose() - chartDataVOS.get(start).getClose());
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
    public static List<Num> getRsi(final List<ChartDataVO> chartDataVOList, int bars) {
        if (bars < 5) {
            bars = 5;
        }
        initBars(chartDataVOList);

        RSIIndicator rsiIndicator = new RSIIndicator(new ClosePriceIndicator(series), bars);
        final List<Num> indicatorResults = new ArrayList<>();
        for (int i = 0; i < chartDataVOList.size(); i++) {
            indicatorResults.add(rsiIndicator.getValue(i));
        }
        return indicatorResults;
    }

    /**
     * Default short bar count: 12 and long bar count: 26
     *
     * @param chartDataVOList
     * @return
     */
    public static List<Num> getMac(final List<ChartDataVO> chartDataVOList) {
        initBars(chartDataVOList);

        MACDIndicator rsiIndicator = new MACDIndicator(new ClosePriceIndicator(series));
        final List<Num> indicatorResults = new ArrayList<>();
        for (int i = 0; i < chartDataVOList.size(); i++) {
            indicatorResults.add(rsiIndicator.getValue(i));
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
}
