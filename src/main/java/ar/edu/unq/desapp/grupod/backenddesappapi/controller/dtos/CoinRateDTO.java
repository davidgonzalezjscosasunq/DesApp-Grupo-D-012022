package ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos;

import ar.edu.unq.desapp.grupod.backenddesappapi.service.types.CoinRate;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class CoinRateDTO {
    @JsonProperty
    private String symbol;
    @JsonProperty
    private LocalDateTime rateDate;
    @JsonProperty
    private Float usdPrice;
    @JsonProperty
    private Float pesosPrice;

    public static CoinRateDTO from(CoinRate coinRate){
        return new CoinRateDTO(coinRate.getSymbol(), coinRate.getRateDate(), coinRate.usdPrice(), coinRate.pesosPrice());
    }

    public CoinRateDTO( String symbol, LocalDateTime rateDate, Float usdPrice, Float pesosPrice){
        this.symbol = symbol;
        this.rateDate = rateDate;
        this.usdPrice = usdPrice;
        this.pesosPrice = pesosPrice;
    }
}
