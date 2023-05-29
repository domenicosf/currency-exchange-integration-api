package com.crewmeister.cmcodingchallenge.service;

import com.crewmeister.cmcodingchallenge.entity.Currency;
import com.crewmeister.cmcodingchallenge.entity.CurrencyExchangeRate;
import com.crewmeister.cmcodingchallenge.entity.CurrencyExchangeRateId;
import com.crewmeister.cmcodingchallenge.exception.InternalServerErrorException;
import com.crewmeister.cmcodingchallenge.repository.CurrencyExchangeRateRepository;
import dto.ConversionResult;
import dto.CurrencyConversionData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CurrencyExchangeRateService {

    private final CurrencyExchangeRateRepository currencyExchangeRateRepository;


    public CurrencyExchangeRateService(CurrencyExchangeRateRepository currencyExchangeRateRepository) {
        this.currencyExchangeRateRepository = currencyExchangeRateRepository;
    }

    public Map<String, Map<String, Float>> formatToMap(CurrencyExchangeRate currencyExchangeRate) {
        Map<String, Float> ERByDate = new HashMap<>() {
            {
                put(currencyExchangeRate.getCurrencyExchangeRateId().getDate(), currencyExchangeRate.getExchangeRate());
            }
        };
        Map<String, Map<String, Float>> result = new HashMap<>() {
            {
                put(currencyExchangeRate.getCurrencyExchangeRateId().getCurrency().getCurrencyId(), ERByDate);
            }
        };
        return result;
    }

    public ResponseEntity<List<CurrencyExchangeRate>> getAllExchangeRates() {
        log.info("Retrieving all the exchange rates!");
        try {
            return new ResponseEntity<>(currencyExchangeRateRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error during the currencies retrieval!");
        }
    }

    public ResponseEntity<List<CurrencyExchangeRate>> getExchangeRatesByDate(String date) {
        log.info("Retrieving all the exchange rates by date!");
        try {
            return new ResponseEntity<>(currencyExchangeRateRepository.findByCurrencyExchangeRateIdDate(date), HttpStatus.OK);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error during the currencies retrieval!");
        }
    }

    public ResponseEntity<ConversionResult> convertToEuro(CurrencyConversionData conversionData) {
        log.info("Converting to Euro!");
        try {
            CurrencyExchangeRate exchangeRate = currencyExchangeRateRepository.findByCurrencyExchangeRateId(new CurrencyExchangeRateId(new Currency(conversionData.getCurrency()), conversionData.getDate()));
            if (exchangeRate == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            String convertedAmount = conversionData.convertAmount(exchangeRate.getExchangeRate());
            return new ResponseEntity<>(new ConversionResult(convertedAmount), HttpStatus.OK);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error during the currencies retrieval!");
        }
    }


}
