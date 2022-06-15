package ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.ActiveCrypto;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.TradedVolume;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.User;

public class    TradedVolumeDTO {

    @JsonProperty
    private Long userId;
    @JsonProperty
    private LocalDateTime dateAndTimeRequest;
    @JsonProperty
    private Float tradedValueInUsd;
    @JsonProperty
    private Float tradedValueInPesos;
    @JsonProperty
    private List<ActiveCrypto> assets;

    public static TradedVolumeDTO from(TradedVolume tradedVolume){
        return new TradedVolumeDTO(tradedVolume.getUserId(), tradedVolume.dateAndTimeRequest(), tradedVolume.tradedValueInUsd(), tradedVolume.tradedValueInPesos(), tradedVolume.assets());
    }

    public TradedVolumeDTO(Long userId, LocalDateTime dateAndTimeRequest, Float tradedValueInUsd, Float tradedValueInPesos, List<ActiveCrypto> assets) {
        this.userId = userId;
        this.dateAndTimeRequest = dateAndTimeRequest;
        this.tradedValueInUsd = tradedValueInUsd;
        this.tradedValueInPesos = tradedValueInPesos;
        this.assets = assets;
    }
}
