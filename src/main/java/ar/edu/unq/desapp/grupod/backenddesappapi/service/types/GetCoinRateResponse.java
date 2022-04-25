package ar.edu.unq.desapp.grupod.backenddesappapi.service.types;

public class GetCoinRateResponse {
    public Float usdPrice;
    public Float pesosPrice;

    public void setPesosPrice(Float pesosPrice) {
        this.pesosPrice = pesosPrice;
    }

    public void setUsdPrice(Float usdPrice) {
        this.usdPrice = usdPrice;
    }
}
