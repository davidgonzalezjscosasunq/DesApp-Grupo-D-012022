package ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PostSellAdvertisementDTO {

    @JsonProperty
    private Long sellerId;

    @JsonProperty
    private String assetSymbol;

    @JsonProperty
    private Integer quantityToSell;

    @JsonProperty
    private Double sellingPrice;

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
