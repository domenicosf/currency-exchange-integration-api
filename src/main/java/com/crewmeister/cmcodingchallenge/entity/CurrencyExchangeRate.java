package com.crewmeister.cmcodingchallenge.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "currency_exchange_rate")
@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CurrencyExchangeRate {
    @EmbeddedId
    private CurrencyExchangeRateId currencyExchangeRateId;
    @Column(name = "exchange_rate")
    private float exchangeRate;
}
