package com.crewmeister.cmcodingchallenge.currency;

import com.crewmeister.cmcodingchallenge.entity.Currency;
import com.crewmeister.cmcodingchallenge.entity.CurrencyExchangeRate;
import com.crewmeister.cmcodingchallenge.service.CurrencyExchangeRateService;
import com.crewmeister.cmcodingchallenge.service.CurrencyService;
import dto.ConversionResult;
import dto.CurrencyConversionData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController()
@RequestMapping("/api/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;
    private final CurrencyExchangeRateService currencyExchangeRateService;

    public CurrencyController(CurrencyService currencyService, CurrencyExchangeRateService currencyExchangeRateService) {
        this.currencyService = currencyService;
        this.currencyExchangeRateService = currencyExchangeRateService;
    }

    @GetMapping
    public ResponseEntity<List<String>> getCurrencies() {
        return currencyService.getAllCurrencies();
    }

    // Get all exchange rates from all currencies at all available times
    @GetMapping("/exchange-rates")
    public ResponseEntity<List<CurrencyExchangeRate>> getExchangeRates() {
        return this.currencyExchangeRateService.getAllExchangeRates();
    }

    // Get all exchange rates from all currencies by date
    @GetMapping("/exchange-rates/search")
    public ResponseEntity<List<CurrencyExchangeRate>> getExchangeRatesByDate(@RequestParam String date) {
        return this.currencyExchangeRateService.getExchangeRatesByDate(date);
    }

    // Convert certain amount of given currency on a given date to Euro
    @PostMapping("/conversion")
    public ResponseEntity<ConversionResult> convertToEuro(@RequestBody CurrencyConversionData conversionData){
        return this.currencyExchangeRateService.convertToEuro(conversionData);
    }
}
