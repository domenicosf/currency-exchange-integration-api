package com.crewmeister.cmcodingchallenge.service;

import com.crewmeister.cmcodingchallenge.exception.InternalServerErrorException;
import com.crewmeister.cmcodingchallenge.repository.CurrencyExchangeRateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyExchangeRateServiceTest {
    @InjectMocks
    private CurrencyExchangeRateService currencyExchangeRateService;
    @Mock
    private CurrencyExchangeRateRepository currencyExchangeRateRepository;

    @Test
    public void testGetExchangeRatesWithInternalServerErrorException() {
        //given
        when(currencyExchangeRateRepository.findAll()).thenThrow(new RuntimeException());

        //then
        InternalServerErrorException thrown = assertThrows(
                InternalServerErrorException.class,
                () -> currencyExchangeRateService.getAllExchangeRates(),
                "Expected doThing() to throw, but it didn't"
        );

        assertTrue(thrown.getMessage().contentEquals("Error during the exchange rates retrieval!"));
    }

    @Test
    public void testGetExchangeRatesByDateWithInternalServerErrorException() {
        //given
        when(currencyExchangeRateRepository.findByCurrencyExchangeRateIdDate(any())).thenThrow(new RuntimeException());

        //then
        InternalServerErrorException thrown = assertThrows(
                InternalServerErrorException.class,
                () -> currencyExchangeRateService.getExchangeRatesByDate(any()),
                "Expected doThing() to throw, but it didn't"
        );

        assertTrue(thrown.getMessage().contentEquals("Error during the exchange rates retrieval by date!"));
    }

    @Test
    public void testConvertToEuroWithInternalServerErrorException() {
        InternalServerErrorException thrown = assertThrows(
                InternalServerErrorException.class,
                () -> currencyExchangeRateService.convertToEuro(null),
                "Expected doThing() to throw, but it didn't"
        );

        assertTrue(thrown.getMessage().contentEquals("Error during the conversion to euro retrieval!"));
    }
}