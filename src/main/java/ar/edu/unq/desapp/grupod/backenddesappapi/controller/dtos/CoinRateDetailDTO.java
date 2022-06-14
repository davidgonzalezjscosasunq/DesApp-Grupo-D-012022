package ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

import ar.edu.unq.desapp.grupod.backenddesappapi.service.types.CoinRateDetail;


public class CoinRateDetailDTO extends CoinRateDTO {
    @JsonProperty
    private String symbol;
    @JsonProperty
    private LocalDateTime rateDate;

    public static CoinRateDetailDTO from(CoinRateDetail coinRateDetail){
        return new CoinRateDetailDTO(coinRateDetail.usdPrice(), coinRateDetail.pesosPrice(), coinRateDetail.symbol(), coinRateDetail.rateDate());
    }

    public CoinRateDetailDTO(Float usdPrice, Float pesosPrice, String symbol, LocalDateTime rateDate) {
        super(usdPrice, pesosPrice);
        this.symbol = symbol;
        this.rateDate = rateDate;
    }
}
