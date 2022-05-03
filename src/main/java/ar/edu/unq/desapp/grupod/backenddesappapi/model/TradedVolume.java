package ar.edu.unq.desapp.grupod.backenddesappapi.model;

import java.time.LocalDateTime;
import java.util.List;

public class TradedVolume {
    private User user;
    private LocalDateTime dateAndTimeRequest;
    private Float tradedValueInUsd;
    private Float tradedValueInPesos;
    private List<ActiveCrypto> assets;

    public TradedVolume(User user, LocalDateTime dateAndTimeRequest, Float tradedValueInUsd, Float tradedValueInPesos, List<ActiveCrypto> assets) {
        this.user = user;
        this.dateAndTimeRequest = dateAndTimeRequest;
        this.tradedValueInUsd = tradedValueInUsd;
        this.tradedValueInPesos = tradedValueInPesos;
        this.assets = assets;
    }

    public User user(){
        return this.user;
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
