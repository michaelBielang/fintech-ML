package com.bachelorthesis.supervised_problem_solving.services.algos;

import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.vo.ChartDataVO;
import org.springframework.stereotype.Service;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class RSI {


    public void calculateEma(final List<ChartDataVO> chartDataVOList, final int bars, final int timeFrame) {
        BarSeries series = new BaseBarSeriesBuilder().withName("RSI").build();
        series.setMaximumBarCount(chartDataVOList.size() + 1);

        for (ChartDataVO chartDataVO : chartDataVOList) {
            //Number openPrice, Number highPrice, Number lowPrice, Number closePrice, Number volume
            series.addBar(ZonedDateTime.of(chartDataVO.getLocalDateTime(), ZoneId.of("UTC")), chartDataVO.getOpen(),
                    chartDataVO.getHigh(), chartDataVO.getLow(), chartDataVO.getClose(), chartDataVO.getQuoteVolume());
        }
        RSIIndicator rsiIndicator = new RSIIndicator(new ClosePriceIndicator(series), bars);
        for (int i = 0; i < timeFrame; i++) {
            System.out.println(rsiIndicator.getValue(i));
        }

    }

}
