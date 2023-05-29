package com.crewmeister.cmcodingchallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CmCodingChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(CmCodingChallengeApplication.class, args);
		/*
		https://api.statistiken.bundesbank.de/rest/download/BBEX3/D.AUD.EUR.BB.AC.000?format=csv&lang=en
https://api.statistiken.bundesbank.de/rest/download/BBEX3/D.BGN.EUR.BB.AC.000?format=csv&lang=en
https://api.statistiken.bundesbank.de/rest/download/BBEX3/D.BRL.EUR.BB.AC.000?format=csv&lang=en
https://api.statistiken.bundesbank.de/rest/download/BBEX3/D.CAD.EUR.BB.AC.000?format=csv&lang=en
https://api.statistiken.bundesbank.de/rest/download/BBEX3/D.CHF.EUR.BB.AC.000?format=csv&lang=en
https://api.statistiken.bundesbank.de/rest/download/BBEX3/D.USD.EUR.BB.AC.000?format=csv&lang=en
https://api.statistiken.bundesbank.de/rest/download/BBEX3/D.MYR.EUR.BB.AC.000?format=csv&lang=en
https://api.statistiken.bundesbank.de/rest/download/BBEX3/D.SEK.EUR.BB.AC.000?format=csv&lang=en
https://api.statistiken.bundesbank.de/rest/download/BBEX3/D.SGD.EUR.BB.AC.000?format=csv&lang=en
https://api.statistiken.bundesbank.de/rest/download/BBEX3/D.NZD.EUR.BB.AC.000?format=csv&lang=en
		 */
	}

}
