package ar.edu.unq.desapp.grupod.backenddesappapi.service.types;

public class CoinRate {
    private Float usdPrice;
    private Float pesosPrice;

    public CoinRate(Float usdPrice, Float pesosPrice){
        this.usdPrice = usdPrice;
        this.pesosPrice = pesosPrice;
    }

    public Float usdPrice(){
        return this.usdPrice;
    }

    public Float pesosPrice(){
        return this.pesosPrice;
    }
}