package com.crewmeister.cmcodingchallenge.service;

import com.crewmeister.cmcodingchallenge.entity.Currency;
import com.crewmeister.cmcodingchallenge.entity.CurrencyExchangeRate;
import com.crewmeister.cmcodingchallenge.entity.CurrencyExchangeRateId;
import com.crewmeister.cmcodingchallenge.repository.CurrencyExchangeRateRepository;
import com.crewmeister.cmcodingchallenge.repository.CurrencyRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.CurrencyData;
import dto.Date;
import dto.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DataLoaderService implements CommandLineRunner {

    public static final String URI_WITHOUT_START_DATE = "/data/BBEX3/D..EUR.BB.AC.000?detail=dataonly";
    public static final String URI_WITH_START_DATE = "/data/BBEX3/D..EUR.BB.AC.000?startPeriod=%s&detail=dataonly";
    public static final String URI_WITH_START_DATE_AND_END_DATE = "/data/BBEX3/D..EUR.BB.AC.000?startPeriod=%s&endPeriod=%s&detail=dataonly";

    private final WebClient webClient;
    private final CurrencyRepository currencyRepository;
    private final CurrencyExchangeRateRepository currencyExchangeRateRepository;

    final
    ObjectMapper objectMapper;

    @Value("${initialize.db.start-date}")
    private String startDate;

    public DataLoaderService(WebClient webClient, CurrencyRepository currencyRepository,
                             CurrencyExchangeRateRepository currencyExchangeRateRepository, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.currencyRepository = currencyRepository;
        this.currencyExchangeRateRepository = currencyExchangeRateRepository;
        this.objectMapper = objectMapper;
    }

    // Use webClient to fetch data from API
    public WebClient.ResponseSpec fetchData(String URI) {
        return webClient.get().uri(URI).retrieve();
    }

    // Fetch all data from API or chose data starting from certain date
    public ResponseData fetchAllData(String startDate) {
        if (startDate != null) {
            return fetchData(String.format(URI_WITH_START_DATE, startDate)).bodyToMono(ResponseData.class).block();
        }
        return fetchData(URI_WITHOUT_START_DATE).bodyToMono(ResponseData.class).block();
    }

    // Fetch latestData from API
    public ResponseData fetchLatestData() {
        String currentDate = LocalDate.now().toString();
        return fetchData(String.format(URI_WITH_START_DATE_AND_END_DATE, currentDate, currentDate)).bodyToMono(ResponseData.class).block();
    }

    //Fill Exchange Rate Table in DB
    public void fillExchangeRate(ResponseData data) {
        List<CurrencyData> currencies = this.extractAllCurrencies(data);
        List<Date> dates = this.extractAllDates(data);
        Map<String, JsonNode> allExchangeRates = this.extractAllExchangeRates(data);
        List<CurrencyExchangeRate> currencyExchangeRates = new ArrayList<>();
        int currencyIndex = 0;

        for (JsonNode value : allExchangeRates.values()) {
            int dateIndex = 0;
            CurrencyExchangeRate currencyExchangeRate = new CurrencyExchangeRate();

            if (value.get("observations") == null) {
                currencyIndex++;
                continue;
            }

            JsonNode oneCurrencyRates = value.get("observations");
            Map<String, List<Float>> oneCurrencyRatesMap = objectMapper.convertValue(oneCurrencyRates, new TypeReference<>() {
            });

            for (List<Float> v : oneCurrencyRatesMap.values()) {

                if (v.get(0) == null) {
                    dateIndex++;
                    continue;
                }

                float exchangeRate = withBigDecimal(v.get(0), 2);
                CurrencyExchangeRateId currencyExchangeRateId = new CurrencyExchangeRateId(new Currency(currencies.get(currencyIndex).getId()),
                        dates.get(dateIndex).getId());
                currencyExchangeRate.setCurrencyExchangeRateId(currencyExchangeRateId);
                currencyExchangeRate.setExchangeRate(exchangeRate);
                currencyExchangeRateRepository.save(currencyExchangeRate);
                dateIndex++;
            }
            currencyIndex++;
        }

    }

    public void initializeCurrencies(ResponseData data) {
        List<CurrencyData> currenciesData = this.extractAllCurrencies(data);
        List<Currency> currencies = currenciesData.stream()
                .map(currencyData -> new Currency(currencyData.getId()))
                .collect(Collectors.toList());
        currencyRepository.saveAll(currencies);
    }

    // Extract available currencies from data
    public List<CurrencyData> extractAllCurrencies(ResponseData data) {
        return data.getData().getStructure().getDimensions().getCurrencies().get(1).getValues();
    }

    // Extract available dates from data
    public List<Date> extractAllDates(ResponseData data) {
        return data.getData().getStructure().getDimensions().getObservation().get(0).getValues();
    }

    // Extract Exchange rates and format result as map
    public Map<String, JsonNode> extractAllExchangeRates(ResponseData data) {
        JsonNode series = data.getData().getDataSets().get(0).getSeries();
        return objectMapper.convertValue(series, new TypeReference<>() {
        });
    }

    public static float withBigDecimal(float value, int places) {
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(places, RoundingMode.HALF_UP);
        return bigDecimal.floatValue();
    }

    // Add latest data available to DB
    public void addLatestDataToDB() {
        ResponseData latestData = this.fetchLatestData();
        this.fillExchangeRate(latestData);
    }

    // Initialize database(currencies table and exchange rate table)
    public void initializeDB(String startDate) {
        try {
            ResponseData allData = this.fetchAllData(startDate);
            this.initializeCurrencies(allData);
            this.fillExchangeRate(allData);
        } catch (Exception e) {
            log.error("Error during the database initialization!", e);
        }
    }

    // Schedule cron job on 4PM every day to update DB with the latest data from API
    @Scheduled(cron = "0 0 17 * * ?", zone = "UTC")
    public void updateDB() {
        this.addLatestDataToDB();
    }


    @Override
    public void run(String... args) {
        log.info("Loading the data from Bundesbank daily exchange rate rest endpoint to the database");
        initializeDB(startDate);
        log.info("Database loaded successfully");
    }

}
