package ar.edu.unq.desapp.grupod.backenddesappapi.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class BuyOrder {
    private Long buyer;
    private Integer quantityToBuy;
    public Long cryptoAssertAdverticement;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //private User buyer;
    //private CryptoAdvertisement cryptoAssertAdverticement;
    //private Integer quantityToBuy;

    private BuyOrder() {}

    public BuyOrder(User buyer, CryptoAdvertisement cryptoAssertAdverticement, Integer quantityToBuy) {
        if (cryptoAssertAdverticement.wasPublishedBy(buyer)) {
            throw new ModelException("Buyer and seller can't be the same user");
        }

        if (quantityToBuy <= 0) {
            throw new ModelException("Quantity to buy must be greater than zero");
        }

        this.buyer = buyer.id();
        this.cryptoAssertAdverticement = cryptoAssertAdverticement.id();
        this.quantityToBuy = quantityToBuy;
    }

    public Boolean wasPlaceBy(User user) {
        return true;
    }

    public Boolean isAssetOwner(User user) {
        return true;
    }

    public Long id() {
        return id;
    }

    public void confirmFor(User seller, CryptoAdvertisement advertisement) {
        assertIsNotTheBuyer(seller);

        advertisement.decreaseQuantity(quantityToBuy);
    }

    private void assertIsNotTheBuyer(User seller) {
        if (seller.id() == buyer) {
            throw new ModelException("A user cannot confirm an order placed by himself");
        }
    }
}
