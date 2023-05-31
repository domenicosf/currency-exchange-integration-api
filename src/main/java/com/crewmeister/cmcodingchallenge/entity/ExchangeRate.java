package com.crewmeister.cmcodingchallenge.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "exchange_rate")
@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ExchangeRate {
    @EmbeddedId
    private ExchangeRateId exchangeRateId;
    @Column(name = "exchange_rate_value")
    private float exchangeRateValue;
}
