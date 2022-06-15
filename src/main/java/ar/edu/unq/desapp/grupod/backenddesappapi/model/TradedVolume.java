package ar.edu.unq.desapp.grupod.backenddesappapi.model;

import java.time.LocalDateTime;
import java.util.List;

public class TradedVolume {
    private Long userId;
    private LocalDateTime dateAndTimeRequest;
    private Float tradedValueInUsd;
    private Float tradedValueInPesos;
    private List<ActiveCrypto> assets;

    public TradedVolume(Long userId, LocalDateTime dateAndTimeRequest, Float tradedValueInUsd, Float tradedValueInPesos, List<ActiveCrypto> assets) {
        this.userId = userId;
        this.dateAndTimeRequest = dateAndTimeRequest;
        this.tradedValueInUsd = tradedValueInUsd;
        this.tradedValueInPesos = tradedValueInPesos;
        this.assets = assets;
    }

    public Long getUserId(){
        return this.userId;
    }

    public LocalDateTime dateAndTimeRequest(){
        return this.dateAndTimeRequest;
    }

    public Float tradedValueInUsd(){
        return this.tradedValueInUsd;
    }

    public Float tradedValueInPesos(){
        return this.tradedValueInPesos;
    }

    public List<ActiveCrypto> assets(){
        return this.assets;
    }

}
