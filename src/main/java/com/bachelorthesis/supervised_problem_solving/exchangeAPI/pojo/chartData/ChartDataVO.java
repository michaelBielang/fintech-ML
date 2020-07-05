package com.bachelorthesis.supervised_problem_solving.exchangeAPI.pojo.chartData;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getQuoteVolume() {
        return quoteVolume;
    }

    public void setQuoteVolume(String quoteVolume) {
        this.quoteVolume = quoteVolume;
    }

    public String getWeightedAverage() {
        return weightedAverage;
    }

    public void setWeightedAverage(String weightedAverage) {
        this.weightedAverage = weightedAverage;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
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
