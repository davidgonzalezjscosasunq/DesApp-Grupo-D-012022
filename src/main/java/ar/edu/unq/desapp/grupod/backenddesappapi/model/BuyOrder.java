package ar.edu.unq.desapp.grupod.backenddesappapi.model;

import java.util.Optional;

public class BuyOrder {

    private User buyer;
    private CryptoAdvertisement cryptoAssertAdverticement;
    private Integer quantityToBuy;

    public BuyOrder(User buyer, CryptoAdvertisement cryptoAssertAdverticement, Integer quantityToBuy) {
        if (cryptoAssertAdverticement.wasPublishedBy(buyer)) {
            throw new ModelException("Buyer and seller can't be the same user");
        }

        if (quantityToBuy <= 0) {
            throw new ModelException("Quantity to buy must be greater than zero");
        }

        this.buyer = buyer;
        this.cryptoAssertAdverticement = cryptoAssertAdverticement;
        this.quantityToBuy = quantityToBuy;
    }

    public Boolean wasPlaceBy(User user) {
        return true;
    }

    public Boolean isAssetOwner(User user) {
        return true;
    }
}
