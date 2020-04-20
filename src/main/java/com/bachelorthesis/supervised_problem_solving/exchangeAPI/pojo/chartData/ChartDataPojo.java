package com.bachelorthesis.supervised_problem_solving.exchangeAPI.pojo.chartData;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
public class ChartDataPojo {

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
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private ChartDataPojo(Builder builder) {
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

        public ChartDataPojo build() {
            return new ChartDataPojo(this);
        }
    }
}
