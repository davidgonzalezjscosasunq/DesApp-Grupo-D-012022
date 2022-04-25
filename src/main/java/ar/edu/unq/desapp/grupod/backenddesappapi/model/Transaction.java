package ar.edu.unq.desapp.grupod.backenddesappapi.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Transaction {
    public static final String PENDING_STATE = "PENDING";
    public static final String CONFIRMED_STATE = "CONFIRMED";

    private Long interestedUser; // TODO: ver por que rompe cuando hago un rename
    private Integer quantity;
    private Long cryptoAssertAdvertisement;
    private String cryptoAssertAdvertisementSymbol;
    private Integer cryptoAssertAdvertisementQuantity;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String state;
    //private User buyer;
    //private CryptoAdvertisement cryptoAssertAdverticement;
    //private Integer quantityToBuy;

    private Transaction() {}

    public Transaction(User interestedUser, AssetAdvertisement cryptoAssetAdverticement, Integer quantity) {
        assertAdvertisementWasNotPublishedBy(interestedUser, cryptoAssetAdverticement);
        assertIsValidQuantity(quantity);

        this.interestedUser = interestedUser.id();
        this.cryptoAssertAdvertisement = cryptoAssetAdverticement.id();
        this.cryptoAssertAdvertisementSymbol = cryptoAssetAdverticement.assetSymbol();
        this.cryptoAssertAdvertisementQuantity = cryptoAssetAdverticement.quantity();
        this.quantity = quantity;
        this.state = PENDING_STATE;
    }

    public Boolean wasInformedBy(User user) {
        return interestedUser == user.id();
    }

    public Boolean isPending() {
        return state == PENDING_STATE;
    }

    public Boolean isConfirmed() {
        return state == CONFIRMED_STATE;
    }

    public Long id() {
        return id;
    }

    public String symbol() {
        return cryptoAssertAdvertisementSymbol;
    }

    public Integer quantity() {
        return quantity;
    }

    public void confirmBy(User seller, AssetAdvertisement advertisement) {
        assertIsNotTheBuyer(seller);

        advertisement.decreaseQuantity(quantity);
        state = CONFIRMED_STATE;
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

    private void assertIsNotTheBuyer(User seller) {
        if (seller.id() == interestedUser) {
            throw new ModelException("A user cannot confirm an order placed by himself");
        }
    }

    public Long cryptoAssertAdvertisementId() { // TODO: quitar cuando este solucionado lo de la persistencia que no anda
        return cryptoAssertAdvertisement;
    }
}
