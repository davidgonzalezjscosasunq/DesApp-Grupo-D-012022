package ar.edu.unq.desapp.grupod.backenddesappapi.model;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.validators.*;

import javax.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String password;
    private String cvu;
    private String criptoWalletAddress;

    private User() {}

    public User(String firstName, String lastName, String email, String address, String password, String cvu, String cryptoWalletAddress) {
        assertIsValidFirstName(firstName);
        assertIsValidLastName(lastName);
        assertIsValidEmail(email);
        assertIsValidCVU(cvu);
        assertIsValidCryptoWalletAddress(cryptoWalletAddress);
        assertIsValidAddress(address);

        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.password = password;
        this.cvu = cvu;
        this.criptoWalletAddress = cryptoWalletAddress;
    }

    private void assertIsValidFirstName(String name) {
        new PersonNameStringValidator().assertIsValidFirstName(name);
    }

    private void assertIsValidLastName(String name) {
        new PersonNameStringValidator().assertIsValidLastName(name);
    }

    private void assertIsValidEmail(String email) {
        new EmailStringValidator().assertIsValid(email);
    }

    private void assertIsValidAddress(String address) {
        new AddressStringValidator().assertIsValid(address);
    }

    private void assertIsValidCVU(String cvu) {
        new CVUStringValidator().assertIsValid(cvu);
    }

    private void assertIsValidCryptoWalletAddress(String cryptoWalletAddress) {
        new CryptoWalletAddressStringValidator().assertIsValid(cryptoWalletAddress);
    }

}
