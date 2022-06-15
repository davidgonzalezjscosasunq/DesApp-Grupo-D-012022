package ar.edu.unq.desapp.grupod.backenddesappapi.controller;

import ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.UserRegistrationDTO;
import ar.edu.unq.desapp.grupod.backenddesappapi.factories.UserTestFactory;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserControllerTest extends ControllerTest {

    @Test
    public void registerUserSuccessfully() {
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
    public void userRegistrationFailsWithBadRequest() {
        var invalidUserRegistrationDTO = createInvalidUserRegistrationDTO();

        var responseEntity = restTemplate.postForEntity(userURL(), new HttpEntity(invalidUserRegistrationDTO), String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(responseEntity.getBody()).isEqualTo("User first name must have between 3 and 30 letters");
    }

    @Test
    public void findUserByIdSuccessfully() {
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

    @Test
    public void findUserByIdWithInvalidIdFailsWithNotFound() {
        var responseEntity = restTemplate.getForEntity(userURLWithId(123L), String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("User not found");
    }


    private UserRegistrationDTO createInvalidUserRegistrationDTO() {
        return new UserRegistrationDTO("", UserTestFactory.JUAN_LAST_NAME, UserTestFactory.JUAN_EMAIL, UserTestFactory.JUAN_ADDRESS, UserTestFactory.JUAN_PASSWORD, UserTestFactory.JUAN_CVU, UserTestFactory.JUAN_CRIPTO_WALLET_ADDRESS);
    }
}
