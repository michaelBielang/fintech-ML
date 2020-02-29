package com.bachelorthesis.supervised_problem_solving.exchangeAPI;

import com.bachelorthesis.supervised_problem_solving.exchangeAPI.enums.Periods;
import com.bachelorthesis.supervised_problem_solving.exchangeAPI.pojo.chartData.ChartDataPojo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Poloniex {

    public static void main(String[] args) {
        Poloniex poloniex = new Poloniex();
        try {
            String currency = poloniex.getAvailableCurrenciesAtExchange().get(0);
            poloniex.getChartData(LocalDateTime.now().minusMonths(3), LocalDateTime.now(), currency,
                    Periods.eighteenHundred.getNumVal());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final static String ROUTE = "https://poloniex.com/public?command=";

    public void getChartData(final LocalDateTime from, final LocalDateTime to, final String currency, final int period) {
        ZoneId zoneId = ZoneId.systemDefault();
        final String command = "returnChartData&currencyPair=";
        final String start = "&start=" + from.atZone(zoneId).toEpochSecond();
        final String end = "&end=" + to.atZone(zoneId).toEpochSecond();
        final String periodCommand = "&period=" + period;
        final String query = ROUTE + command + currency + start + end + periodCommand;

        List<ChartDataPojo> openOrdersSingleCurrencyPOJO = new LinkedList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            openOrdersSingleCurrencyPOJO = objectMapper.readValue(sendRequest(query), new TypeReference<List<ChartDataPojo>>() {
            });
        } catch (IOException e) {
            System.out.println("blubb");
        }
        openOrdersSingleCurrencyPOJO.forEach(entry -> {
            System.out.println("#######");
            System.out.println(entry);
        });
    }

    public List<String> getAvailableCurrenciesAtExchange() throws IOException {
        String reply = "";
        final String queryArgs = "https://poloniex.com/public?command=returnTicker";
        reply = sendRequest(queryArgs);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(reply);

        ArrayList<String> currencies = new ArrayList<>();
        jsonNode.fieldNames().forEachRemaining(currencies::add);
        Collections.sort(currencies);
        return currencies;

    }

    public String sendRequest(final String queryArgs) throws IOException {
        URL obj = new URL(queryArgs);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setConnectTimeout(15 * 1000);
        con.setReadTimeout(15 * 1000);
        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String result = in.readLine();
        con.disconnect();

        return result;
    }
}
