package com.crewmeister.cmcodingchallenge.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CurrencyExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private LocalDate date;
    @NotNull
    private Double rate;
    @NotNull
    private String currency;
}
