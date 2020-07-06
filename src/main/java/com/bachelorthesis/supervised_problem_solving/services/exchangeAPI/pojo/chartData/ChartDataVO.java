package com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.pojo.chartData;

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
    private String high;
    private String low;
    private String open;
    private String close;
    private String volume;
    private String quoteVolume;
    private String weightedAverage;

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
        private String high;
        private String low;
        private String open;
        private String close;
        private String volume;
        private String quoteVolume;
        private String weightedAverage;

        public Builder() {
        }

        public Builder date(long val) {
            date = val;
            return this;
        }

        public Builder high(String val) {
            high = val;
            return this;
        }

        public Builder low(String val) {
            low = val;
            return this;
        }

        public Builder open(String val) {
            open = val;
            return this;
        }

        public Builder close(String val) {
            close = val;
            return this;
        }

        public Builder volume(String val) {
            volume = val;
            return this;
        }

        public Builder quoteVolume(String val) {
            quoteVolume = val;
            return this;
        }

        public Builder weightedAverage(String val) {
            weightedAverage = val;
            return this;
        }

        public ChartDataVO build() {
            return new ChartDataVO(this);
        }
    }
}
