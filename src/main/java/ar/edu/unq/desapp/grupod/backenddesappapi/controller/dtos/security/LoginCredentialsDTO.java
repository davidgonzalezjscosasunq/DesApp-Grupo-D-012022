package ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.security;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginCredentialsDTO {

    @JsonProperty
    private String email;

    @JsonProperty
    private String password;

    public String email() {
        return email;
    }

    public String password() {
        return password;
    }
}
