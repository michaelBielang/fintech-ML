package com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex;

import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.enums.Periods;
import com.bachelorthesis.supervised_problem_solving.services.exchangeAPI.poloniex.vo.ChartDataVO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Thread.sleep;
import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class PoloniexApiService {

    private final static Logger LOGGER = LoggerFactory.getLogger(PoloniexApiService.class);
    private final static String ROUTE = "https://poloniex.com/public?command=";
    private static final int TIMEOUT = 15 * 1000;
    private static final int MINIMUM_POLL_WINDOW_IN_MS = 250;

    private long lastApiCall;
    private LocalDateTime lastCurrencyListUpdate;
    private List<String> currencyList;

    @PostConstruct
    public void init() {
        try {
            lastCurrencyListUpdate = LocalDateTime.now().minusDays(2);
            this.currencyList = getAvailableCurrenciesAtExchange();
        } catch (IOException | InterruptedException exception) {
            LOGGER.error("Could not init currencyList", exception);
        }
        lastCurrencyListUpdate = LocalDateTime.now();
    }

    /**
     * getChartData(LocalDateTime.now().minusMonths(3), LocalDateTime.now(), currency, Periods.eighteenHundred.getNumVal());
     *
     * @param from
     * @param to
     * @param currency
     * @param period
     * @return
     */
    public List<ChartDataVO> getChartData(final LocalDateTime from, final LocalDateTime to, final String currency, final Periods period) throws IOException, InterruptedException {

        final String command = "returnChartData&currencyPair=";
        final String start = "&start=" + from.atZone(ZoneId.systemDefault()).toEpochSecond();
        final String end = "&end=" + to.atZone(ZoneId.systemDefault()).toEpochSecond();
        final String periodCommand = "&period=" + period.getPeriodValue();
        final String query = ROUTE + command + currency + start + end + periodCommand;

        final List<ChartDataVO> openOrdersSingleCurrencyPOJO = new ObjectMapper().readValue(sendRequest(query), new TypeReference<>() {
        });

        setDateAndCurrency(openOrdersSingleCurrencyPOJO, currency);
        return openOrdersSingleCurrencyPOJO;
    }

    private void setDateAndCurrency(final List<ChartDataVO> openOrdersSingleCurrencyPOJO, final String currency) {
        openOrdersSingleCurrencyPOJO.forEach(entry -> {
            entry.setLocalDateTime(LocalDateTime
                    .ofEpochSecond(entry.getDate(), 0, ZoneOffset.UTC));
            entry.setCurrency(currency);
        });
    }

    public List<String> getAvailableCurrenciesAtExchange() throws IOException, InterruptedException {
        if (DAYS.between(lastCurrencyListUpdate, LocalDateTime.now()) < 2 && currencyList.size() > 0) {
            return currencyList;
        }
        final String queryArgs = "https://poloniex.com/public?command=returnTicker";
        final String reply = sendRequest(queryArgs);
        final JsonNode jsonNode = new ObjectMapper().readTree(reply);

        final ArrayList<String> currencies = new ArrayList<>();

        jsonNode.fieldNames()
                .forEachRemaining(currencies::add);

        Collections.sort(currencies);
        lastCurrencyListUpdate = LocalDateTime.now();
        return currencies;
    }

    private String sendRequest(final String queryArgs) throws IOException, InterruptedException {
        //prevent ip blocking from Poloniex
        if (System.currentTimeMillis() - lastApiCall < MINIMUM_POLL_WINDOW_IN_MS) {
            sleep(System.currentTimeMillis() - lastApiCall);
        }

        final URL obj = new URL(queryArgs);
        final HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setConnectTimeout(TIMEOUT);
        con.setReadTimeout(TIMEOUT);
        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");

        final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        final String result = in.readLine();
        con.disconnect();
        lastApiCall = System.currentTimeMillis();

        return result;
    }
}
