package ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionCreationDTO {
    @JsonProperty
    private Long interestedUserId;

    @JsonProperty
    private Long advertisementId;

    @JsonProperty
    private Integer quantityToTransfer;

    public Long interestedUserId() {
        return interestedUserId;
    }

    public Long advertisementId() {
        return advertisementId;
    }

    public Integer quantityToTransfer() {
        return quantityToTransfer;
    }
}
