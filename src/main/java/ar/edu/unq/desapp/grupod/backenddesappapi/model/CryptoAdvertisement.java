package ar.edu.unq.desapp.grupod.backenddesappapi.model;

import javax.persistence.*;

@Entity
public class CryptoAdvertisement {

    public static final String BUY_ADVERTISE_TYPE = "BUY_ADVERTISE";
    public static final String SELL_ADVERTISE_TYPE = "CELL_ADVERTISE";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String type;
    private String cryptoActiveSymbol; // TODO: ver por que rompe cuando hago un rename
    private Integer quantity;
    private Double price;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "publisher_id", referencedColumnName = "id")
//    private User publisher;

    private String publisherFirstName;
    private String publisherLastName;

    private CryptoAdvertisement() {}

    public CryptoAdvertisement(String type, String symbol, Integer quantity, Double price, User publisher) {
        assertIsValidQuantity(quantity);
        assertIsValidPrice(price);

        this.type = type;
        this.cryptoActiveSymbol = symbol;
        this.quantity = quantity;
        this.price = price;
        //this.publisher = publisher; // TODO: arreglar. Genera problemas porque la ejecucion del metodo del service no es transaccional

        this.publisherFirstName = publisher.firstName();
        this.publisherLastName = publisher.lastName();
    }

    public Long id() {
        return id;
    }

    public String publisherFirstName() {
        //return publisher.firstName();
        return publisherFirstName;
    }

    public String publisherLastName() {
        //return publisher.lastName();
        return publisherLastName;
    }

    public String assetSymbol() {
        return cryptoActiveSymbol;
    }

    public int quantity() {
        return quantity;
    }

    public double price() {
        return price;
    }

    private void assertIsValidQuantity(Integer quantityToSell) {
        if (quantityToSell < 1) throw new ModelException("Quantity cannot be lesser than 1");
    }

    private void assertIsValidPrice(Double sellingPrice) {
        if (sellingPrice <= 0) throw new ModelException("Price amount of money must be positive");
    }

    public boolean wasPublishedBy(User user) {
        return publisherFirstName.equals(user.firstName());
    }

    public void decreaseQuantity(Integer quantityToDecrease) {
        quantity -= quantityToDecrease;
    }

    public boolean isSoldOut() {
        return quantity == 0;
    }

    public boolean isABuy() {
        return type == BUY_ADVERTISE_TYPE;
    }
}
