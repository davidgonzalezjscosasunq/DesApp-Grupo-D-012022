package ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InformTransactionDTO {

    @JsonProperty
    private Long interestedUserId;

    @JsonProperty
    private Long advertisementId;

    @JsonProperty
    private int quantityToTransfer;

    public InformTransactionDTO(Long interestedUserId, Long advertisementId, int quantityToTransfer) {
        this.interestedUserId = interestedUserId;
        this.advertisementId = advertisementId;
        this.quantityToTransfer = quantityToTransfer;
    }

    public Long interestedUserId() {
        return interestedUserId;
    }

    public Long advertisementId() {
        return advertisementId;
    }

    public int quantityToTransfer() {
        return quantityToTransfer;
    }
}
