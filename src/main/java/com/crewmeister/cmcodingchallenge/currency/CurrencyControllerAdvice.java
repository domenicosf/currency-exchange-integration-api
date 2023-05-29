package com.crewmeister.cmcodingchallenge.currency;

import com.crewmeister.cmcodingchallenge.exception.ErrorResponse;
import com.crewmeister.cmcodingchallenge.exception.InternalServerErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@ControllerAdvice
public class CurrencyControllerAdvice {

    @ExceptionHandler(value = {InternalServerErrorException.class})
    protected ResponseEntity<Object> handleInternalServerError(InternalServerErrorException internalServerErrorException, WebRequest webRequest){
        ErrorResponse response = new ErrorResponse(internalServerErrorException.getMessage(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
