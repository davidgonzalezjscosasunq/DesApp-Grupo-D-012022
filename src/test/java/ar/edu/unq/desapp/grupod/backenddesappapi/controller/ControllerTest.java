package ar.edu.unq.desapp.grupod.backenddesappapi.controller;

import ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.user.UserDTO;
import ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.user.UserRegistrationDTO;
import ar.edu.unq.desapp.grupod.backenddesappapi.factories.UserTestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract public class ControllerTest {

    @Autowired
    protected UserController controller;

    @LocalServerPort
    protected int port;

    @Autowired
    protected TestRestTemplate restTemplate;

    protected String baseURL() {
        return "http://localhost:" + port;
    }

    protected String userURL() {
        return baseURL() + "/users";
    }

    protected String userURLWithId(Long userId) {
        return userURL() + "/" + userId;
    }

    protected String assetAdvertisementURL() {
        return baseURL() + "/advertisements";
    }

    protected UserRegistrationDTO createPepeGomezRegistrationDTO() {
        return new UserRegistrationDTO(UserTestFactory.PEPE_FIRST_NAME, UserTestFactory.PEPE_LAST_NAME, UserTestFactory.JUAN_EMAIL, UserTestFactory.JUAN_ADDRESS, UserTestFactory.JUAN_PASSWORD, UserTestFactory.JUAN_CVU, UserTestFactory.JUAN_CRIPTO_WALLET_ADDRESS);
    }

    protected UserDTO registerUser(UserRegistrationDTO pepeGomezRegistrationDTO) {
        return restTemplate.postForObject(userURL(), new HttpEntity(pepeGomezRegistrationDTO), UserDTO.class);
    }

    protected UserDTO registerPepeGomez() {
        return registerUser(createPepeGomezRegistrationDTO());
    }

    protected UserDTO findUserById(Long userId) {
        return restTemplate.getForObject(userURLWithId(userId), UserDTO.class);
    }
}
