package ar.edu.unq.desapp.grupod.backenddesappapi.service.types;

import java.time.LocalDateTime;

public class CoinRateDetail extends CoinRate{
    private String symbol;
    private LocalDateTime rateDate;

    public String symbol(){
      return this.symbol;
    }

    public LocalDateTime rateDate(){
        return this.rateDate;
    }

    public CoinRateDetail(Float usdPrice, Float pesosPrice, String symbol, LocalDateTime rateDate) {
        super(usdPrice, pesosPrice);
        this.symbol = symbol;
        this.rateDate = rateDate;
    }
}
