package ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos;

import ar.edu.unq.desapp.grupod.backenddesappapi.service.types.CoinRate;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CoinRateDTO {

    @JsonProperty
    private Float usdPrice;
    @JsonProperty
    private Float pesosPrice;

    public static CoinRateDTO form(CoinRate coinRate){
        return new CoinRateDTO(coinRate.usdPrice(), coinRate.pesosPrice());
    }

    public CoinRateDTO(Float usdPrice, Float pesosPrice){
        this.usdPrice = usdPrice;
        this.pesosPrice = pesosPrice;
    }
}
