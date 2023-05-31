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

    public WebClient.ResponseSpec retrieveData(String URI) {
        return webClient.get().uri(URI).retrieve();
    }

    public ResponseData retrieveAllData(String startDate) {
        if (startDate != null) {
            return retrieveData(String.format(URI_WITH_START_DATE, startDate)).bodyToMono(ResponseData.class).block();
        }
        return retrieveData(URI_WITHOUT_START_DATE).bodyToMono(ResponseData.class).block();
    }

    public ResponseData retrieveLatestData() {
        String currentDate = LocalDate.now().toString();
        return retrieveData(String.format(URI_WITH_START_DATE_AND_END_DATE, currentDate, currentDate)).bodyToMono(ResponseData.class).block();
    }

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

    public List<CurrencyData> extractAllCurrencies(ResponseData data) {
        return data.getData().getStructure().getDimensions().getCurrencies().get(1).getValues();
    }

    public List<Date> extractAllDates(ResponseData data) {
        return data.getData().getStructure().getDimensions().getObservation().get(0).getValues();
    }

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

    public void addLatestDataToDataBase() {
        ResponseData latestData = this.retrieveLatestData();
        this.fillExchangeRate(latestData);
    }

    public void initializeDataBase(String startDate) {
        try {
            ResponseData allData = this.retrieveAllData(startDate);
            this.initializeCurrencies(allData);
            this.fillExchangeRate(allData);
        } catch (Exception e) {
            log.error("Error during the database initialization!", e);
        }
    }

    @Scheduled(cron = "0 0 17 * * ?", zone = "UTC")
    public void updateDB() {
        this.addLatestDataToDataBase();
    }


    @Override
    public void run(String... args) {
        log.info("Loading the data from Bundesbank daily exchange rate rest endpoint to the database");
        initializeDataBase(startDate);
        log.info("Database loaded successfully");
    }

}
