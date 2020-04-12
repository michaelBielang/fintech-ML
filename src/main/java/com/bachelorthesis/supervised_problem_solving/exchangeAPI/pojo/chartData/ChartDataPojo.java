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
}
