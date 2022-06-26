package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class UsersLookupTest extends ServiceTest {

    @Test
    void aRegisteredUserCanBeFoundById() {
        var aUser = registerUser();

        var foundUser = userService.findUserById(aUser.id());

        assertIsTheSameUser(foundUser, aUser);
    }

    @Test
    void cannotFoundAUserWithANotRegisteredUserId() {
        var notRegisteredUserId = -999l;

        assertThrowsDomainException(
                "user.not_found",
                () -> userService.findUserById(notRegisteredUserId));
    }

    @Test
    void allRegisteredUsersCanBeFound() {
        var aUser = registerUser();

        var foundUsers = userService.findUsers();

        assertThat(foundUsers.size()).isEqualTo(1);
        assertIsTheSameUser(foundUsers.get(0), aUser);
    }

}
