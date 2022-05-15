package ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.asset_advertisement;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.AssetAdvertisementType;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AssetAdvertisementCreationDTO {
    @JsonProperty
    private AssetAdvertisementType advertisementType;

    @JsonProperty
    private Long publisherId;

    @JsonProperty
    private String assetSymbol;

    @JsonProperty
    private Integer quantity;

    @JsonProperty
    private Double price;

    public AssetAdvertisementCreationDTO(AssetAdvertisementType advertisementType, Long publisherId, String assetSymbol, Integer quantity, Double price) {
        this.advertisementType = advertisementType;
        this.publisherId = publisherId;
        this.assetSymbol = assetSymbol;
        this.quantity = quantity;
        this.price = price;
    }

    public AssetAdvertisementType advertisementType() {
        return advertisementType;
    }

    public Long publisherId() {
        return publisherId;
    }

    public String assetSymbol() {
        return assetSymbol;
    }

    public Integer quantity() {
        return quantity;
    }

    public Double price() {
        return price;
    }
}
