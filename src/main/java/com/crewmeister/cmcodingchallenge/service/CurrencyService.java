package com.crewmeister.cmcodingchallenge.service;

import com.crewmeister.cmcodingchallenge.entity.Currency;
import com.crewmeister.cmcodingchallenge.exception.InternalServerErrorException;
import com.crewmeister.cmcodingchallenge.repository.CurrencyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public ResponseEntity<List<String>> getAllCurrencies(){
        log.info("retrieving all the currencies registered!");
        try{
            List<String> currencies = currencyRepository.findAll()
                    .stream()
                    .map(Currency::getCurrencyId)
                    .collect(Collectors.toList());

            if(currencies.isEmpty()){
                return new ResponseEntity<>(currencies, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(currencies, HttpStatus.OK);

        }catch(Exception e){
            throw new InternalServerErrorException("Error during the currencies retrieval!");
        }
    }
}
