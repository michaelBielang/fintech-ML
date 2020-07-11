package com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.vo;

import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.enums.Periods;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "CHART_DATA_VOS")
public class ChartDataVO {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long date;
    private double high;
    private double low;
    private double open;
    private double close;
    private double volume;
    private double quoteVolume;
    private double weightedAverage;
    private Periods periods;

    @JsonIgnore
    private String currency;
    @JsonIgnore
    private LocalDateTime localDateTime;

    public ChartDataVO() {
    }

    private ChartDataVO(Builder builder) {
        setDate(builder.date);
        setHigh(builder.high);
        setLow(builder.low);
        setOpen(builder.open);
        setClose(builder.close);
        setVolume(builder.volume);
        setQuoteVolume(builder.quoteVolume);
        setWeightedAverage(builder.weightedAverage);
    }

    public static final class Builder {
        private long date;
        private double high;
        private double low;
        private double open;
        private double close;
        private double volume;
        private double quoteVolume;
        private double weightedAverage;

        public Builder() {
        }

        public Builder date(long val) {
            date = val;
            return this;
        }

        public Builder high(double val) {
            high = val;
            return this;
        }

        public Builder low(double val) {
            low = val;
            return this;
        }

        public Builder open(double val) {
            open = val;
            return this;
        }

        public Builder close(double val) {
            close = val;
            return this;
        }

        public Builder volume(double val) {
            volume = val;
            return this;
        }

        public Builder quoteVolume(double val) {
            quoteVolume = val;
            return this;
        }

        public Builder weightedAverage(double val) {
            weightedAverage = val;
            return this;
        }

        public ChartDataVO build() {
            return new ChartDataVO(this);
        }
    }
}
