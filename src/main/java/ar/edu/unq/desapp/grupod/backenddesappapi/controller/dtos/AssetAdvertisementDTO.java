package ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.AssetAdvertisement;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.AssetAdvertisementType;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class AssetAdvertisementDTO {

    @JsonProperty
    private Long id;

    @JsonProperty
    private AssetAdvertisementType type;

    @JsonProperty
    private String assetSymbol;

    @JsonProperty
    private Integer quantity;

    @JsonProperty
    private Double price;

    @JsonProperty
    private LocalDateTime publicationLocalDateTime;

    @JsonProperty
    private Long publisherId;

    @JsonProperty
    private Integer publisherReputationPoints;

    public static AssetAdvertisementDTO form(AssetAdvertisement assetAdvertisement) {
        return new AssetAdvertisementDTO(assetAdvertisement.id(), assetAdvertisement.type(), assetAdvertisement.assetSymbol(), assetAdvertisement.quantity(), assetAdvertisement.price(), assetAdvertisement.publicationLocalDateTime(), assetAdvertisement.publisher());
    }

    public AssetAdvertisementDTO() {}

    public AssetAdvertisementDTO(Long assetAdvertisementId, AssetAdvertisementType type, String assetSymbol, int quantity, double price, LocalDateTime publicationLocalDateTime, User publisher) {
        this.id = assetAdvertisementId;
        this.type = type;
        this.assetSymbol = assetSymbol;
        this.quantity = quantity;
        this.price = price;
        this.publicationLocalDateTime = publicationLocalDateTime;
        this.publisherId = publisher.id();
        this.publisherReputationPoints = publisher.reputationPoints();
    }

    public Long id() {
        return id;
    }

    public AssetAdvertisementType type() {
        return type;
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

    public LocalDateTime publicationLocalDateTime() {
        return publicationLocalDateTime;
    }

    public Long publisherId() {
        return publisherId;
    }
}
