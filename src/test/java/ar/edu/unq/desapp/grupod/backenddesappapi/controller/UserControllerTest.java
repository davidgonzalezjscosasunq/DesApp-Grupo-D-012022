package ar.edu.unq.desapp.grupod.backenddesappapi.controller;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserControllerTest extends ControllerTest {

    @Test
    public void aUserCanBeRegistered() {
        var pepeGomezRegistrationDTO = createPepeGomezRegistrationDTO();

        var response = registerUser(pepeGomezRegistrationDTO);

        assertThat(response.firstName()).isEqualTo(pepeGomezRegistrationDTO.firstName());
        assertThat(response.lastName()).isEqualTo(pepeGomezRegistrationDTO.lastName());
        assertThat(response.email()).isEqualTo(pepeGomezRegistrationDTO.email());
        assertThat(response.address()).isEqualTo(pepeGomezRegistrationDTO.address());
        assertThat(response.cvu()).isEqualTo(pepeGomezRegistrationDTO.cvu());
        assertThat(response.cryptoWalletAddress()).isEqualTo(pepeGomezRegistrationDTO.cryptoWalletAddress());
    }

    @Test
    public void aRegisteredUserCanBeFoundById() {
        var pepeGomezRegistrationDTO = createPepeGomezRegistrationDTO();
        var userRegistrationResponse = registerUser(pepeGomezRegistrationDTO);

        var response = findUserById(userRegistrationResponse.id());

        assertThat(response.firstName()).isEqualTo(pepeGomezRegistrationDTO.firstName());
        assertThat(response.lastName()).isEqualTo(pepeGomezRegistrationDTO.lastName());
        assertThat(response.email()).isEqualTo(pepeGomezRegistrationDTO.email());
        assertThat(response.address()).isEqualTo(pepeGomezRegistrationDTO.address());
        assertThat(response.cvu()).isEqualTo(pepeGomezRegistrationDTO.cvu());
        assertThat(response.cryptoWalletAddress()).isEqualTo(pepeGomezRegistrationDTO.cryptoWalletAddress());
    }

}
