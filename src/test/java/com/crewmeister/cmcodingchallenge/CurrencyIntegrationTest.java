package com.crewmeister.cmcodingchallenge;

import com.crewmeister.cmcodingchallenge.entity.CurrencyExchangeRate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ConversionResult;
import dto.CurrencyConversionData;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static io.netty.handler.codec.http.HttpHeaders.Values.APPLICATION_JSON;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CurrencyIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
    }

    @Test
    public void testGetAllCurrencies() {
        get("/api/currencies")
                .then()
                .assertThat()
                .body("$", hasSize(greaterThan(1)))
                .contentType(APPLICATION_JSON)
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void testGetExchangeRates() {
        CurrencyExchangeRate[] currencyExchangeRates = get("/api/currencies/exchange-rates")
                .then()
                .contentType(APPLICATION_JSON)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(CurrencyExchangeRate[].class);

        Assertions.assertTrue(currencyExchangeRates.length > 0);
        Assertions.assertNotNull(currencyExchangeRates[0].getCurrencyExchangeRateId().getCurrency());
        Assertions.assertNotNull(currencyExchangeRates[0].getCurrencyExchangeRateId().getDate());
        Assertions.assertNotNull(currencyExchangeRates[0].getExchangeRate());
    }

    @Test
    public void testGetExchangeRatesByDate() {
        CurrencyExchangeRate[] currencyExchangeRates = given()
                .when()
                .queryParam("date", LocalDate.now().minus(2, ChronoUnit.DAYS).toString())
                .get("/api/currencies/exchange-rates/search")
                .then()
                .contentType(APPLICATION_JSON)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(CurrencyExchangeRate[].class);

        Assertions.assertTrue(currencyExchangeRates.length > 0);
        Assertions.assertNotNull(currencyExchangeRates[0].getCurrencyExchangeRateId().getCurrency());
        Assertions.assertNotNull(currencyExchangeRates[0].getCurrencyExchangeRateId().getDate());
        Assertions.assertNotNull(currencyExchangeRates[0].getExchangeRate());
    }

    @Test
    public void testPostConvertToEuro() throws JsonProcessingException {
        ConversionResult conversionResult = given()
                .header("Content-Type",APPLICATION_JSON)
                .header("Accept", APPLICATION_JSON)
                .body(mapper.writeValueAsString(new CurrencyConversionData(50.76, LocalDate.now().minus(2, ChronoUnit.DAYS).toString(), "GBP")))
                .when()
                .post("/api/currencies/conversion")
                .then()
                .contentType(APPLICATION_JSON)
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ConversionResult.class);

        Assertions.assertNotNull(conversionResult);
        Assertions.assertNotNull(conversionResult.getConvertedAmount());
        Assertions.assertNotNull(Float.valueOf(conversionResult
                .getConvertedAmount()
                .replace("€", "")
                .replace(",", ".")) > 0);
    }
}
