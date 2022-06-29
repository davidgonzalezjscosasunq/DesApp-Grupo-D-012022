package ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginDTO {
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
