package ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfirmTransactionDTO {

    @JsonProperty
    private Long userId;

    @JsonProperty
    private Long transactionToConfirmId;

    public Long userId() {
        return userId;
    }

    public Long transactionToConfirmId() {
        return transactionToConfirmId;
    }

}
