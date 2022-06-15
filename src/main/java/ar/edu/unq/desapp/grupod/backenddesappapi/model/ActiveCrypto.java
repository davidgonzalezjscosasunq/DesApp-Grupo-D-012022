package ar.edu.unq.desapp.grupod.backenddesappapi.model;

public class ActiveCrypto {
    private String symbol;
    private Float nominalAmount;
    private Float currentPriceInUsd;
    private Float currentPriceInPesos;

    public ActiveCrypto(String symbol, Float nominalAmount, Float currentPriceInUsd, Float currentPriceInPesos){
        this.symbol = symbol;
        this.nominalAmount = nominalAmount;
        this.currentPriceInUsd = currentPriceInUsd;
        this.currentPriceInPesos = currentPriceInPesos;
    }

    public String getSymbol(){
        return this.symbol;
    }

    public Float getNominalAmount(){
        return this.nominalAmount;
    }

    public Float getCurrentPriceInUsd(){
        return this.currentPriceInUsd;
    }

    public Float getCurrentPriceInPesos(){
        return this.currentPriceInPesos;
    }

    public Float getFinalPriceInUSD(){
        return this.nominalAmount * this.currentPriceInUsd;
    }

    public Float getFinalPriceInPesos(){
        return this.nominalAmount * this.currentPriceInPesos;
    }
}
