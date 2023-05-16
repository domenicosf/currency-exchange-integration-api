package dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class CurrencyExchangeRateDto {
    private Long id;
    private LocalDate date;
    private Double rate;
    private String currency;
}
