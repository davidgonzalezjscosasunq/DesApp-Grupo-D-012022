package ar.edu.unq.desapp.grupod.backenddesappapi.service.types;

public class CoinRate {
    public Float usdPrice;
    public Float pesosPrice;

    public CoinRate(Float usdPrice, Float pesosPrice){
        this.usdPrice = usdPrice;
        this.pesosPrice = pesosPrice;
    }
}
