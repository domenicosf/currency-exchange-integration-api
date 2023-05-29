package dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CurrencyConversionData {
    private double amount;
    private String date;
    private String currency;

    public String convertAmount(float exchangeRate){
        double convertedAmount = this.amount/exchangeRate;
        return  String.format("%.3f",convertedAmount)+ "€";
    }
}
