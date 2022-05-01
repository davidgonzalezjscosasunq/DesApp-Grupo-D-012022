package ar.edu.unq.desapp.grupod.backenddesappapi.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private TransactionState state;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "interested_user_id", referencedColumnName = "id")
    private User interestedUser;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "asset_advertisement_id", referencedColumnName = "id")
    private AssetAdvertisement assetAdvertisement;

    private Integer quantity;
    private LocalDateTime startLocalDateTime;

    private Transaction() {}

    public Transaction(User interestedUser, AssetAdvertisement assetAdvertisement, Integer quantity, LocalDateTime startLocalDateTime) {
        assertAdvertisementWasNotPublishedBy(interestedUser, assetAdvertisement);
        assertIsValidQuantity(quantity);

        this.interestedUser = interestedUser;
        this.assetAdvertisement = assetAdvertisement;
        this.quantity = quantity;
        this.startLocalDateTime = startLocalDateTime;

        this.state = TransactionState.PENDING_STATE;
    }

    public Boolean wasInformedBy(User user) {
        return interestedUser.id().equals(user.id());
    }

    public Boolean isPending() {
        return state.equals(TransactionState.PENDING_STATE);
    }

    public Boolean isConfirmed() {
        return state.equals(TransactionState.CONFIRMED_STATE);
    }

    public Boolean isCancelled() {
        return state.equals(TransactionState.CANCELLED_STATE);
    }

    public Long id() {
        return id;
    }

    public String assetSymbol() {
        return assetAdvertisement.assetSymbol();
    }

    public Integer quantity() {
        return quantity;
    }

    public User publisher() {
        return assetAdvertisement.publisher();
    }

    public LocalDateTime startLocalDateTime() {
        return startLocalDateTime;
    }

    public void confirmBy(User user) {
        assertIsThePublisher(user);

        assetAdvertisement.decreaseQuantity(quantity);
        state = TransactionState.CONFIRMED_STATE;
    }

    public void cancelBy(User user) {
        assertCanBeCancelledBy(user);

        state = TransactionState.CANCELLED_STATE;
    }

    private boolean canBeCancelledBy(User user) {
        return wasInformedBy(user) || assetAdvertisement.wasPublishedBy(user);
    }

    private void assertAdvertisementWasNotPublishedBy(User user, AssetAdvertisement cryptoAssetAdvertisement) {
        if (cryptoAssetAdvertisement.wasPublishedBy(user)) {
            throw new ModelException("A user cannot inform a transaction for an advertisement published by himself");
        }
    }

    private void assertIsValidQuantity(Integer quantity) {
        if (quantity <= 0) {
            throw new ModelException("A transaction quantity must be greater than zero");
        }
    }

    private void assertIsThePublisher(User user) {
        if (!assetAdvertisement.wasPublishedBy(user)) {
            throw new ModelException("A user cannot confirm a transaction for an advertisement not published by himself");
        }
    }

    private void assertCanBeCancelledBy(User user) {
        if (!canBeCancelledBy(user)) {
            throw new ModelException("A transaction can only be cancelled by the user that informed it or the user that published the advertisement");
        }
    }

    public Optional<String> paymentAddress() {
        return state.paymentAddressFor(assetAdvertisement);
    }
}
