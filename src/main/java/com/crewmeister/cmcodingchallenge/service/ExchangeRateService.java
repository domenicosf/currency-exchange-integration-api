package com.crewmeister.cmcodingchallenge.service;

import com.crewmeister.cmcodingchallenge.entity.Currency;
import com.crewmeister.cmcodingchallenge.entity.ExchangeRate;
import com.crewmeister.cmcodingchallenge.entity.ExchangeRateId;
import com.crewmeister.cmcodingchallenge.exception.InternalServerErrorException;
import com.crewmeister.cmcodingchallenge.repository.ExchangeRateRepository;
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
public class ExchangeRateService {

    private final ExchangeRateRepository exchangeRateRepository;


    public ExchangeRateService(ExchangeRateRepository exchangeRateRepository) {
        this.exchangeRateRepository = exchangeRateRepository;
    }

    public Map<String, Map<String, Float>> formatToMap(ExchangeRate exchangeRate) {
        Map<String, Float> ERByDate = new HashMap<>() {
            {
                put(exchangeRate.getExchangeRateId().getDate(), exchangeRate.getExchangeRateValue());
            }
        };
        Map<String, Map<String, Float>> result = new HashMap<>() {
            {
                put(exchangeRate.getExchangeRateId().getCurrency().getCurrencyId(), ERByDate);
            }
        };
        return result;
    }

    public ResponseEntity<List<ExchangeRate>> getAllExchangeRates() {
        log.info("Retrieving all the exchange rates!");
        try {
            return new ResponseEntity<>(exchangeRateRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error during the exchange rates retrieval!");
        }
    }

    public ResponseEntity<List<ExchangeRate>> getExchangeRatesByDate(String date) {
        log.info("Retrieving all the exchange rates by date!");
        try {
            return new ResponseEntity<>(exchangeRateRepository.findByExchangeRateIdDate(date), HttpStatus.OK);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error during the exchange rates retrieval by date!");
        }
    }

    public ResponseEntity<ConversionResult> convertToEuro(CurrencyConversionData conversionData) {
        log.info("Converting to Euro!");
        try {
            ExchangeRate exchangeRate = exchangeRateRepository.findByExchangeRateId(new ExchangeRateId(new Currency(conversionData.getCurrency()), conversionData.getDate()));
            if (exchangeRate == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            String convertedAmount = conversionData.convertAmount(exchangeRate.getExchangeRateValue());
            return new ResponseEntity<>(new ConversionResult(convertedAmount), HttpStatus.OK);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error during the conversion to euro retrieval!");
        }
    }


}
