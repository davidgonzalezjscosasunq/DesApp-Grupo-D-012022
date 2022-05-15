package ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.transaction;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.Transaction;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.TransactionState;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Optional;

public class TransactionDTO {
    @JsonProperty
    private Long id;

    @JsonProperty
    private TransactionState state;

    @JsonProperty
    private String assetSymbol;

    @JsonProperty
    private Integer quantity;

    @JsonProperty
    private LocalDateTime startLocalDateTime;

    @JsonProperty
    private Optional<String> paymentAddress;

    @JsonProperty
    private Long interestedUserId;

    @JsonProperty
    private Long assetAdvertisementPublisher;

    public static TransactionDTO form(Transaction transaction) {
        var interestedUser = transaction.interestedUser();
        var assetAdvertisementPublisher = transaction.publisher();

        return new TransactionDTO(
                transaction.id(),
                transaction.state(),
                transaction.assetSymbol(),
                transaction.quantity(),
                transaction.startLocalDateTime(),
                transaction.paymentAddress(),
                interestedUser.id(),
                assetAdvertisementPublisher.id()
        );
    }

    public TransactionDTO(Long id, TransactionState state, String assetSymbol, Integer quantity, LocalDateTime startLocalDateTime, Optional<String> paymentAddress, Long interestedUserId, Long assetAdvertisementPublisher) {
        this.id = id;
        this.state = state;
        this.assetSymbol = assetSymbol;
        this.quantity = quantity;
        this.startLocalDateTime = startLocalDateTime;
        this.paymentAddress = paymentAddress;
        this.interestedUserId = interestedUserId;
        this.assetAdvertisementPublisher = assetAdvertisementPublisher;
    }
}
