package ar.edu.unq.desapp.grupod.backenddesappapi.model;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.validators.*;

import javax.persistence.*;

@Entity
@Table(name="users")
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
    private String cryptoWalletAddress;
    private Integer points;
    private Integer numberOfOperations;

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
        this.cryptoWalletAddress = cryptoWalletAddress;
        this.points = 0;
        this.numberOfOperations = 0;
    }

    public Long id() {
        return id;
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public String cvu() {
        return cvu;
    }

    public String address() {
        return address;
    }

    public String email() {
        return email;
    }

    public String password() {
        return password;
    }

    public String cryptoActiveWalletAddress() {
        return cryptoWalletAddress;
    }

    public Integer reputationPoints() {
        return points;
    }

    public Integer numberOfOperations() {
        return numberOfOperations;
    }

    public void receiveReputationPointsForOperation(int pointsToGain) {
        points += pointsToGain;
        incrementNumberOfOperationsCounter();
    }

    public void looseReputationPointsForOperation(Integer pointsToLoose) {
        points -= pointsToLoose;
        incrementNumberOfOperationsCounter();
    }

    private void incrementNumberOfOperationsCounter() {
        numberOfOperations += 1;
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
