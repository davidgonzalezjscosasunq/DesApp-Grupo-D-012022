package ar.edu.unq.desapp.grupod.backenddesappapi.controller;

import ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.UserDTO;
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

        var userDTO = registerUser(pepeGomezRegistrationDTO);

        assertUserDTOCorrespondToUserRegistrationDTO(userDTO, pepeGomezRegistrationDTO);
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

        var foundUserDTO = findUserById(userRegistrationResponse.id());

        assertUserDTOCorrespondToUserRegistrationDTO(foundUserDTO, pepeGomezRegistrationDTO);
    }

    @Test
    public void findUserByIdWithInvalidIdFailsWithNotFound() {
        var responseEntity = restTemplate.getForEntity(userURLWithId(123L), String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isEqualTo("User not found");
    }

    @Test
    public void findAllUsers() {
        var userRegistrationDTO = createPepeGomezRegistrationDTO();
        registerUser(userRegistrationDTO);

        var responseEntity = restTemplate.getForEntity(userURL(), UserDTO[].class);;

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        var foundUserDTO = responseEntity.getBody()[0];
        assertUserDTOCorrespondToUserRegistrationDTO(foundUserDTO, userRegistrationDTO);
        assertThat(foundUserDTO.numberOfOperations()).isEqualTo(0);
        assertThat(foundUserDTO.reputationPoints()).isEqualTo(0);
    }


    private UserRegistrationDTO createInvalidUserRegistrationDTO() {
        return new UserRegistrationDTO("", UserTestFactory.JUAN_LAST_NAME, UserTestFactory.JUAN_EMAIL, UserTestFactory.JUAN_ADDRESS, UserTestFactory.JUAN_PASSWORD, UserTestFactory.JUAN_CVU, UserTestFactory.JUAN_CRIPTO_WALLET_ADDRESS);
    }

    private void assertUserDTOCorrespondToUserRegistrationDTO(UserDTO userDTO, UserRegistrationDTO userRegistrationDTO) {
        assertThat(userDTO.firstName()).isEqualTo(userRegistrationDTO.firstName());
        assertThat(userDTO.lastName()).isEqualTo(userRegistrationDTO.lastName());
        assertThat(userDTO.email()).isEqualTo(userRegistrationDTO.email());
        assertThat(userDTO.address()).isEqualTo(userRegistrationDTO.address());
        assertThat(userDTO.cvu()).isEqualTo(userRegistrationDTO.cvu());
        assertThat(userDTO.cryptoWalletAddress()).isEqualTo(userRegistrationDTO.cryptoWalletAddress());
    }

}
