package ar.edu.unq.desapp.grupod.backenddesappapi.model;

public class ActiveCrypto {
    public String symbol;
    public Float nominalAmount;
    public Float currentPriceInUsd;
    public Float currentPriceInPesos;

    public ActiveCrypto(String symbol, Float nominalAmount, Float currentPriceInUsd, Float currentPriceInPesos){
        this.symbol = symbol;
        this.nominalAmount = nominalAmount;
        this.currentPriceInUsd = currentPriceInUsd;
        this.currentPriceInPesos = currentPriceInPesos;
    }
}
