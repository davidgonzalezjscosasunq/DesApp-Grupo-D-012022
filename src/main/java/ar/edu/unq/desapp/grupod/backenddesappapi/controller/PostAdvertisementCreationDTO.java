package ar.edu.unq.desapp.grupod.backenddesappapi.controller;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.AssetAdvertisementType;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PostAdvertisementCreationDTO {

    @JsonProperty
    AssetAdvertisementType advertisementType;

    @JsonProperty
    Long sellerId;

    @JsonProperty
    String assetSymbol;

    @JsonProperty
    Integer quantityToSell;

    @JsonProperty
    Double sellingPrice;

    public AssetAdvertisementType advertisementType() {
        return advertisementType;
    }

    public Long sellerId() {
        return sellerId;
    }

    public String assetSymbol() {
        return assetSymbol;
    }

    public Integer quantityToSell() {
        return quantityToSell;
    }

    public Double sellingPrice() {
        return sellingPrice;
    }
}
