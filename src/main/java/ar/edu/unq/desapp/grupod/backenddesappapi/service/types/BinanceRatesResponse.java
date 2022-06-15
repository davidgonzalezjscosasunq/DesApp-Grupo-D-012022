package ar.edu.unq.desapp.grupod.backenddesappapi.service.types;

public class BinanceRatesResponse {
    private String symbol;
    private Float price;

    public Float priceInDollars() {
        return this.price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
