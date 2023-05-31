package com.crewmeister.cmcodingchallenge.service;

import com.crewmeister.cmcodingchallenge.exception.InternalServerErrorException;
import com.crewmeister.cmcodingchallenge.repository.CurrencyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {

    @InjectMocks
    private CurrencyService currencyService;
    @Mock
    private CurrencyRepository currencyRepository;

    @Test
    public void testGetAllCurrenciesWithInternalServerErrorException(){
        //given
        when(currencyRepository.findAll()).thenThrow(new RuntimeException());

        //then
        InternalServerErrorException thrown = assertThrows(
                InternalServerErrorException.class,
                () -> currencyService.getAllCurrencies(),
                "Expected doThing() to throw, but it didn't"
        );

        assertTrue(thrown.getMessage().contentEquals("Error during the currencies retrieval!"));
    }

}