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
    private String cryptoAssertAdverticementSymbol;
    private Integer cryptoAssertAdverticementQuantity;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //private User buyer;
    //private CryptoAdvertisement cryptoAssertAdverticement;
    //private Integer quantityToBuy;

    private BuyOrder() {}

    public BuyOrder(User buyer, CryptoAdvertisement cryptoAssetAdverticement, Integer quantityToBuy) {
        assertAdvertisementWasNotPublishedByBuyer(buyer, cryptoAssetAdverticement);
        assertIsValidQuantity(quantityToBuy);

        this.buyer = buyer.id();
        this.cryptoAssertAdverticement = cryptoAssetAdverticement.id();
        this.cryptoAssertAdverticementSymbol = cryptoAssetAdverticement.cryptoActiveSymbol();
        this.cryptoAssertAdverticementQuantity = cryptoAssetAdverticement.quantity();
        this.quantityToBuy = quantityToBuy;
    }

    public Boolean wasPlaceBy(User user) {
        return buyer == user.id();
    }

    public Long id() {
        return id;
    }


    public String symbol() {
        return cryptoAssertAdverticementSymbol;
    }

    public void confirmFor(User seller, CryptoAdvertisement advertisement) {
        assertIsNotTheBuyer(seller);

        advertisement.decreaseQuantity(quantityToBuy);
    }

    private void assertAdvertisementWasNotPublishedByBuyer(User buyer, CryptoAdvertisement cryptoAssetAdverticement) {
        if (cryptoAssetAdverticement.wasPublishedBy(buyer)) {
            throw new ModelException("Buyer and seller can't be the same user");
        }
    }

    private void assertIsValidQuantity(Integer quantityToBuy) {
        if (quantityToBuy <= 0) {
            throw new ModelException("Quantity to buy must be greater than zero");
        }
    }

    private void assertIsNotTheBuyer(User seller) {
        if (seller.id() == buyer) {
            throw new ModelException("A user cannot confirm an order placed by himself");
        }
    }

    public Integer quantity() {
        return cryptoAssertAdverticementQuantity;
    }
}
