package com.crewmeister.cmcodingchallenge.repository;

import com.crewmeister.cmcodingchallenge.entity.CurrencyExchangeRate;
import com.crewmeister.cmcodingchallenge.entity.CurrencyExchangeRateId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CurrencyExchangeRateRepository extends JpaRepository<CurrencyExchangeRate, CurrencyExchangeRateId> {
    List<CurrencyExchangeRate> findByCurrencyExchangeRateIdDate(String date);
    CurrencyExchangeRate findByCurrencyExchangeRateId(CurrencyExchangeRateId id);
}
