package ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.Transaction;
import ar.edu.unq.desapp.grupod.backenddesappapi.service.TradingService;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateTransactionDTO {

    @JsonProperty
    private Long userId;

    @JsonProperty
    private Long transactionId;

    @JsonProperty
    private UpdateTransactionType type;

    public static UpdateTransactionDTO confirmationFor(UserDTO userDTO, TransactionDTO transactionDTO) {
        return new UpdateTransactionDTO(userDTO.id(), transactionDTO.transactionId(), UpdateTransactionType.CONFIRM);
    }

    public static UpdateTransactionDTO cancellationFor(UserDTO userDTO, TransactionDTO transactionDTO) {
        return new UpdateTransactionDTO(userDTO.id(), transactionDTO.transactionId(), UpdateTransactionType.CANCEL);
    }

    public UpdateTransactionDTO() {}

    public UpdateTransactionDTO(Long userId, Long transactionId, UpdateTransactionType type) {
        this.userId = userId;
        this.transactionId = transactionId;
        this.type = type;
    }

    public Long userId() {
        return userId;
    }

    public Long transactionId() {
        return transactionId;
    }

    public UpdateTransactionType type() {
        return type;
    }

    public Transaction applyTo(TradingService tradingService) {
        switch (type) {
            case CONFIRM:
                return tradingService.confirmTransaction(userId, transactionId);
            case CANCEL:
                return tradingService.cancelTransaction(userId, transactionId);
        }
        throw new RuntimeException("Unknown transaction states");
    }

}
