package ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.user;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserRegistrationDTO {

    @JsonProperty
    private String firstName;
    @JsonProperty
    private String lastName;
    @JsonProperty
    private String email;
    @JsonProperty
    private String address;
    @JsonProperty
    private String password;
    @JsonProperty
    private String cvu;
    @JsonProperty
    private String cryptoWalletAddress;

    public UserRegistrationDTO(String firstName, String lastName, String email, String address, String password, String cvu, String cryptoWalletAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.password = password;
        this.cvu = cvu;
        this.cryptoWalletAddress = cryptoWalletAddress;
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public String email() {
        return email;
    }

    public String address() {
        return address;
    }

    public String password() {
        return password;
    }

    public String cvu() {
        return cvu;
    }

    public String cryptoWalletAddress() {
        return cryptoWalletAddress;
    }

}
