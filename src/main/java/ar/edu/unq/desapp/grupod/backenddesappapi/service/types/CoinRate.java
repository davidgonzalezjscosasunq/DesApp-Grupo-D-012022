package ar.edu.unq.desapp.grupod.backenddesappapi.service.types;

import java.time.LocalDateTime;

public class CoinRate {
    private String symbol;
    private LocalDateTime rateDate;
    private Float usdPrice;
    private Float pesosPrice;

    public CoinRate(String symbol, LocalDateTime rateDate, Float usdPrice, Float pesosPrice){
        this.symbol = symbol;
        this.rateDate = rateDate;
        this.usdPrice = usdPrice;
        this.pesosPrice = pesosPrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public LocalDateTime getRateDate() {
        return rateDate;
    }

    public Float usdPrice(){
        return this.usdPrice;
    }

    public Float pesosPrice(){
        return this.pesosPrice;
    }
}
