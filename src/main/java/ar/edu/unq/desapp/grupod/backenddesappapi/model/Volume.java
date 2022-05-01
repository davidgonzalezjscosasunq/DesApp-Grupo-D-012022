package ar.edu.unq.desapp.grupod.backenddesappapi.model;

import java.time.LocalDateTime;
import java.util.List;

public class Volume {
    public User user;
    public LocalDateTime dateAndTimeRequest;
    public Float tradedValueInUsd;
    public Float tradedValueInPesos;
    public List<ActiveCrypto> assets;

    public Volume(User user, LocalDateTime dateAndTimeRequest, Float tradedValueInUsd,  Float tradedValueInPesos, List<ActiveCrypto> assets) {
        this.user = user;
        this.dateAndTimeRequest = dateAndTimeRequest;
        this.tradedValueInUsd = tradedValueInUsd;
        this.tradedValueInPesos = tradedValueInPesos;
        this.assets = assets;
    }
}
