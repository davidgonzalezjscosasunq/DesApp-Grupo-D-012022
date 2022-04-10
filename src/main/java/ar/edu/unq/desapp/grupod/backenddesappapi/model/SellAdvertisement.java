package ar.edu.unq.desapp.grupod.backenddesappapi.model;

import javax.persistence.*;

@Entity
public class SellAdvertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String cryptoActiveSymbol;
    private Integer quantity;
    private Double price;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "publisher_id", referencedColumnName = "id")
//    private User publisher;

    private String publisherFirstName;
    private String publisherLastName;

    private SellAdvertisement() {}

    public SellAdvertisement(String cryptoActiveSymbol, Integer quantity, Double price, User publisher) {
        assertIsValidQuantity(quantity);
        assertIsValidPrice(price);

        this.cryptoActiveSymbol = cryptoActiveSymbol;
        this.quantity = quantity;
        this.price = price;
        //this.publisher = publisher; // TODO: arreglar. Genera problemas porque la ejecucion del metodo del service no es transaccional
        this.publisherFirstName = publisher.firstName();
        this.publisherLastName = publisher.lastName();
    }

    public String publisherFirstName() {
        //return publisher.firstName();
        return publisherFirstName;
    }

    public String publisherLastName() {
        //return publisher.lastName();
        return publisherLastName;
    }

    public String cryptoActiveSymbol() {
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
}
