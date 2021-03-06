package ar.edu.unq.desapp.grupod.backenddesappapi.controller;

import ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.UserDTO;
import ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.UserRegistrationDTO;
import ar.edu.unq.desapp.grupod.backenddesappapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(value = "/users/{userId}")
    public ResponseEntity<UserDTO> findUserById(@PathVariable("userId") Long userId) {
        var user = userService.findUserById(userId);

        return new ResponseEntity<UserDTO>(UserDTO.form(user), HttpStatus.OK);
    }

    @GetMapping(value = "/users")
    public ResponseEntity<List<UserDTO>> findUsers() {
        var users = userService.findUsers();
        var userDTOs = users.stream().map(UserDTO::form).collect(Collectors.toList());

        return new ResponseEntity<>(userDTOs, HttpStatus.OK);
    }

    @PostMapping(value = "/users")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        var user = userService.registerUser(userRegistrationDTO.firstName(), userRegistrationDTO.lastName(), userRegistrationDTO.email(), userRegistrationDTO.address(), userRegistrationDTO.password(), userRegistrationDTO.cvu(), userRegistrationDTO.cryptoWalletAddress());

        return new ResponseEntity<UserDTO>(UserDTO.form(user), HttpStatus.CREATED);
    }

}
