package ar.edu.unq.desapp.grupod.backenddesappapi.service.types;

public class GetCoinRateResponse {
    public Float usdPrice;
    public Float pesosPrice;

    public GetCoinRateResponse(Float usdPrice, Float pesosPrice){
        this.usdPrice = usdPrice;
        this.pesosPrice = pesosPrice;
    }
}
