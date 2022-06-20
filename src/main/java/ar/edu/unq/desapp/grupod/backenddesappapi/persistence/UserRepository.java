package ar.edu.unq.desapp.grupod.backenddesappapi.persistence;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.EntityNotFoundException;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);

    default User findByEmailOrThrow(String email) {
        return findByEmail(email).orElseThrow(() -> new EntityNotFoundException("user.not_found"));
    }
}
