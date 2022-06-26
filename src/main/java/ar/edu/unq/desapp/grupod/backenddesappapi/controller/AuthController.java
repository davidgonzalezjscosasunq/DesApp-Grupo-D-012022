package ar.edu.unq.desapp.grupod.backenddesappapi.controller;

import ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.UserDTO;
import ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.UserRegistrationDTO;
import ar.edu.unq.desapp.grupod.backenddesappapi.controller.dtos.security.LoginCredentialsDTO;
import ar.edu.unq.desapp.grupod.backenddesappapi.service.UserService;
import ar.edu.unq.desapp.grupod.backenddesappapi.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/auth/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserRegistrationDTO userRegistrationDTO) {
        var encodedPassword = passwordEncoder.encode(userRegistrationDTO.password());
        var user = userService.registerUser(userRegistrationDTO.firstName(), userRegistrationDTO.lastName(), userRegistrationDTO.email(), userRegistrationDTO.address(), encodedPassword, userRegistrationDTO.cvu(), userRegistrationDTO.cryptoWalletAddress());

        return new ResponseEntity<>(UserDTO.form(user), HttpStatus.CREATED);
    }

    @PostMapping(value = "/auth/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginCredentialsDTO loginCredentialsDTO) {
        var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginCredentialsDTO.email(), loginCredentialsDTO.password());
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        var token = jwtUtils.createToken(loginCredentialsDTO.email());

        return ResponseEntity.ok(token);
    }

}

