package com.crewmeister.cmcodingchallenge.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="currency")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Currency {

    @Id
    @Column(name = "currency_id")
    private String currencyId;

}
