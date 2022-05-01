package ar.edu.unq.desapp.grupod.backenddesappapi.model;

import javax.persistence.*;
import java.util.Optional;

@Entity
public class AssetAdvertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AssetAdvertisementType type;

    private String assetSymbol;
    private Integer quantity;
    private Double price;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "publisher_id", referencedColumnName = "id")
    private User publisher;

    private AssetAdvertisement() {}

    public AssetAdvertisement(AssetAdvertisementType type, String symbol, Integer quantity, Double price, User publisher) {
        assertIsValidQuantity(quantity);
        assertIsValidPrice(price);

        this.type = type;
        this.assetSymbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.publisher = publisher;
    }

    public boolean wasPublishedBy(User user) {
        return publisher.id().equals(user.id());
    }

    public Long id() {
        return id;
    }

    public String assetSymbol() {
        return assetSymbol;
    }

    public int quantity() {
        return quantity;
    }

    public double price() {
        return price;
    }

    public User publisher() {
        return publisher;
    }

    public void decreaseQuantity(Integer quantityToDecrease) {
        quantity -= quantityToDecrease;
    }

    private void assertIsValidQuantity(Integer quantityToSell) {
        if (quantityToSell < 1) throw new ModelException("Quantity cannot be lesser than 1");
    }

    private void assertIsValidPrice(Double sellingPrice) {
        if (sellingPrice <= 0) throw new ModelException("Price amount of money must be positive");
    }

    public String paymentAddress() {
        return type.equals(AssetAdvertisementType.SELL_ADVERTISEMENT)
                ? publisher().cvu()
                : publisher.cryptoActiveWalletAddress();
    }
}
