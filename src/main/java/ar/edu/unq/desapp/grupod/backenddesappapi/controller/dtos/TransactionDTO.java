package ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.Transaction;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.TransactionState;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionDTO {

    @JsonProperty
    private Long transactionId;

    @JsonProperty
    private String assetSymbol;

    @JsonProperty
    private TransactionState state;

    public static TransactionDTO from(Transaction transaction) {
        return new TransactionDTO(transaction.id(), transaction.assetSymbol(), transaction.state());
    }

    public TransactionDTO() {}

    public TransactionDTO(Long transactionId, String assetSymbol, TransactionState state) {
        this.transactionId = transactionId;
        this.assetSymbol = assetSymbol;
        this.state = state;
    }

    public Long transactionId() {
        return transactionId;
    }

    public String assetSymbol() {
        return assetSymbol;
    }

    public String state() {
        return state.toString();
    }
}
