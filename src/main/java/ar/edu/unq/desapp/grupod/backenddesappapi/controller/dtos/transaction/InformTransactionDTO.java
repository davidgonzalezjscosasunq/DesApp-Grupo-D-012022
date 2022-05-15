package ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InformTransactionDTO {

    @JsonProperty
    private Long interestedUserId;

    @JsonProperty
    private int quantityToTransfer;

    public Long interestedUserId() {
        return interestedUserId;
    }

    public int quantityToTransfer() {
        return quantityToTransfer;
    }

}
