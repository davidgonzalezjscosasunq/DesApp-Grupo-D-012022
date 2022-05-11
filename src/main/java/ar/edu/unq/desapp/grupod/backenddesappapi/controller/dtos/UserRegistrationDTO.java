package ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos;

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
