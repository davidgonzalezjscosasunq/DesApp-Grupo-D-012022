package ar.edu.unq.desapp.grupod.backenddesappapi.model;


import javax.persistence.*;

@Entity
public class Transaction {
    public static final String PENDING_STATE = "PENDING";
    public static final String CONFIRMED_STATE = "CONFIRMED";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String state;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "interested_user_id", referencedColumnName = "id")
    private User interestedUser;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "asset_advertisement_id", referencedColumnName = "id")
    private AssetAdvertisement assetAdvertisement;

    private Integer quantity;

    private Transaction() {}

    public Transaction(User interestedUser, AssetAdvertisement assetAdverticement, Integer quantity) {
        assertAdvertisementWasNotPublishedBy(interestedUser, assetAdverticement);
        assertIsValidQuantity(quantity);

        this.interestedUser = interestedUser;
        this.assetAdvertisement = assetAdverticement;
        this.quantity = quantity;

        this.state = PENDING_STATE;
    }

    public Boolean wasInformedBy(User user) {
        return interestedUser.id() == user.id();
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

    public String assetSymbol() {
        return assetAdvertisement.assetSymbol();
    }

    public Integer quantity() {
        return quantity;
    }

    public void confirmBy(User user) {
        assertIsThePublisher(user);

        assetAdvertisement.decreaseQuantity(quantity);
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

    private void assertIsThePublisher(User user) {
        if (!assetAdvertisement.wasPublishedBy(user)) {
            throw new ModelException("A user cannot confirm a transaction for an advertisement not published by himself");
        }
    }

}
