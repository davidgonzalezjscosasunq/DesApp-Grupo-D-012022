package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.User;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(String firstName, String lastName, String email, String address, String password, String cvu, String criptoWalletAddress) {
        var newUser = new User(firstName, lastName, email, address, password, cvu, criptoWalletAddress);

        return userRepository.save(newUser);
    }

    public Boolean hasUserRegisteredWithEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
