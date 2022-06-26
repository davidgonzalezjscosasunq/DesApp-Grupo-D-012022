package ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserDTO {

    @JsonProperty
    private Long id;
    @JsonProperty
    private String firstName;
    @JsonProperty
    private String lastName;
    @JsonProperty
    private String email;
    @JsonProperty
    private String address;
    @JsonProperty
    private String cvu;
    @JsonProperty
    private String cryptoWalletAddress;
    @JsonProperty
    private Integer numberOfOperations;
    @JsonProperty
    private Integer reputationPoints;

    public static UserDTO form(User user) {
        return new UserDTO(user.id(), user.firstName(), user.lastName(), user.email(), user.address(), user.cvu(), user.cryptoActiveWalletAddress(), user.numberOfOperations(), user.reputationPoints());
    }

    public UserDTO(Long id, String firstName, String lastName, String email, String address, String cvu, String cryptoWalletAddress, Integer numberOfOperations, Integer reputationPoints) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address = address;
        this.cvu = cvu;
        this.cryptoWalletAddress = cryptoWalletAddress;
        this.numberOfOperations = numberOfOperations;
        this.reputationPoints = reputationPoints;
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

    public String email() {
        return email;
    }

    public String address() {
        return address;
    }

    public String cvu() {
        return cvu;
    }

    public String cryptoWalletAddress() {
        return cryptoWalletAddress;
    }

    public Integer numberOfOperations() {
        return numberOfOperations;
    }

    public Integer reputationPoints() {
        return reputationPoints;
    }
}
