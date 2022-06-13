package ar.edu.unq.desapp.grupod.backenddesappapi.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "TRANSACTIONS")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TransactionState state;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "interested_user_id", referencedColumnName = "id")
    private User interestedUser;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "asset_advertisement_id", referencedColumnName = "id")
    private AssetAdvertisement assetAdvertisement;

    private Integer quantity;

    @Column(name = "start_local_date_time")
    private LocalDateTime startLocalDateTime;

    private Transaction() {}

    public Transaction(User interestedUser, AssetAdvertisement assetAdvertisement, Integer quantity, LocalDateTime startLocalDateTime) {
        assertAdvertisementWasNotPublishedBy(interestedUser, assetAdvertisement);
        assertIsValidQuantity(quantity);

        this.interestedUser = interestedUser;
        this.assetAdvertisement = assetAdvertisement;
        this.quantity = quantity;
        this.startLocalDateTime = startLocalDateTime;

        this.state = TransactionState.PENDING;
    }

    public TransactionState state() {
        return state;
    }

    public Boolean wasInformedBy(User user) {
        return interestedUser.id().equals(user.id());
    }

    public Boolean isPending() {
        return state.equals(TransactionState.PENDING);
    }

    public Boolean isConfirmed() {
        return state.equals(TransactionState.CONFIRMED);
    }

    public Boolean isCancelled() {
        return state.equals(TransactionState.CANCELLED);
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

    public Optional<String> paymentAddress() {
        return state.paymentAddressFor(assetAdvertisement);
    }

    public void confirmBy(User user) {
        assertIsThePublisher(user);

        assetAdvertisement.decreaseQuantity(quantity);
        state = TransactionState.CONFIRMED;
    }

    public void cancelBy(User user) {
        assertCanBeCancelledBy(user);

        state = TransactionState.CANCELLED;
    }

    private boolean canBeCancelledBy(User user) {
        return wasInformedBy(user) || assetAdvertisement.wasPublishedBy(user);
    }

    private void assertAdvertisementWasNotPublishedBy(User user, AssetAdvertisement cryptoAssetAdvertisement) {
        if (cryptoAssetAdvertisement.wasPublishedBy(user)) {
            throw new ModelException("transaction.user_cannot_inform_a_transaction_for_an_advertisement_published_by_himself");
        }
    }

    private void assertIsValidQuantity(Integer quantity) {
        if (quantity <= 0) {
            throw new ModelException("transaction.quantity_must_be_greater_than_zero");
        }
    }

    private void assertIsThePublisher(User user) {
        if (!assetAdvertisement.wasPublishedBy(user)) {
            throw new ModelException("transaction.user_cannot_confirm_a_transaction_for_an_advertisement_published_by_himself");
        }
    }

    private void assertCanBeCancelledBy(User user) {
        if (!canBeCancelledBy(user)) {
            throw new ModelException("transaction.can_only_be_cancelled_by_the_user_that_informed_it_or_the_user_that_published_the_advertisement");
        }
    }

}
