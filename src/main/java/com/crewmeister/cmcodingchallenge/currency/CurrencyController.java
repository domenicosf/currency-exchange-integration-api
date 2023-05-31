package com.crewmeister.cmcodingchallenge.currency;

import com.crewmeister.cmcodingchallenge.entity.ExchangeRate;
import com.crewmeister.cmcodingchallenge.service.ExchangeRateService;
import com.crewmeister.cmcodingchallenge.service.CurrencyService;
import dto.ConversionResult;
import dto.CurrencyConversionData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;
    private final ExchangeRateService exchangeRateService;

    public CurrencyController(CurrencyService currencyService, ExchangeRateService exchangeRateService) {
        this.currencyService = currencyService;
        this.exchangeRateService = exchangeRateService;
    }

    @GetMapping
    public ResponseEntity<List<String>> getCurrencies() {
        return currencyService.getAllCurrencies();
    }

    // Get all exchange rates from all currencies at all available times
    @GetMapping("/exchange-rates")
    public ResponseEntity<List<ExchangeRate>> getExchangeRates() {
        return this.exchangeRateService.getAllExchangeRates();
    }

    // Get all exchange rates from all currencies by date
    @GetMapping("/exchange-rates/search")
    public ResponseEntity<List<ExchangeRate>> getExchangeRatesByDate(@RequestParam String date) {
        return this.exchangeRateService.getExchangeRatesByDate(date);
    }

    // Convert certain amount of given currency on a given date to Euro
    @PostMapping("/conversion")
    public ResponseEntity<ConversionResult> convertToEuro(@RequestBody CurrencyConversionData conversionData){
        return this.exchangeRateService.convertToEuro(conversionData);
    }
}
