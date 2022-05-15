package ar.edu.unq.desapp.grupod.backenddesappapi.controller;

import ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.user.UserDTO;
import ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.user.UserRegistrationDTO;
import ar.edu.unq.desapp.grupod.backenddesappapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(value = "/users/{userId}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable("userId") Long userId) {
        var user = userService.findUserById(userId);

        return new ResponseEntity<UserDTO>(UserDTO.from(user), HttpStatus.OK);
    }

    @PostMapping(value = "/users")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        var user = userService.registerUser(userRegistrationDTO.firstName(), userRegistrationDTO.lastName(), userRegistrationDTO.email(), userRegistrationDTO.address(), userRegistrationDTO.password(), userRegistrationDTO.cvu(), userRegistrationDTO.cryptoWalletAddress());

        return new ResponseEntity<UserDTO>(UserDTO.from(user), HttpStatus.CREATED);
    }

}
