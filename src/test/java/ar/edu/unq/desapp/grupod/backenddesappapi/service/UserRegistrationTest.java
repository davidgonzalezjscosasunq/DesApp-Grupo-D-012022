package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import ar.edu.unq.desapp.grupod.backenddesappapi.factories.UserTestFactory;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserRegistrationTest extends ServiceTest {

    @Test
    void aUserCanBeRegistered() {
        var user = userService.registerUser(UserTestFactory.VALID_FIRST_NAME, UserTestFactory.VALID_LAST_NAME, UserTestFactory.VALID_EMAIL, UserTestFactory.VALID_ADDRESS, UserTestFactory.VALID_PASSWORD, UserTestFactory.VALID_CVU, UserTestFactory.VALID_CRIPTO_WALLET_ADDRESS);

        assertHasUsers(userRepository);
    }

    @Test
    void aUserCanFoundById() {
        var registeredUser = userService.registerUser(UserTestFactory.VALID_FIRST_NAME, UserTestFactory.VALID_LAST_NAME, UserTestFactory.VALID_EMAIL, UserTestFactory.VALID_ADDRESS, UserTestFactory.VALID_PASSWORD, UserTestFactory.VALID_CVU, UserTestFactory.VALID_CRIPTO_WALLET_ADDRESS);

        var foundUser = userService.findUserById(registeredUser.id());

        assertEquals(registeredUser.firstName(), foundUser.firstName());
        assertEquals(registeredUser.lastName(), foundUser.lastName());
    }

    @Test
    void aUserFirstNameCannotHaveLessThan3Letters() {
        var anInvalidNameWith2Letters = "ab";

        assertThrowsDomainException(
                "User first name must have between 3 and 30 letters",
                () -> registerUserWithFirstName(anInvalidNameWith2Letters));

        assertHasNoUsers(userRepository);
    }

    @Test
    void aUserFirstNameCannotHaveMoreThan30Letters() {
        var anInvalidNameWith31Letters = "abcdefghijabcdefghijabcdefghijk";

        assertThrowsDomainException("User first name must have between 3 and 30 letters",
                () -> registerUserWithFirstName(anInvalidNameWith31Letters));

        assertHasNoUsers(userRepository);
    }

    @Test
    void aUserLastNameCannotHaveLessThan3Letters() {
        var anInvalidNameWith2Letters = "ab";

        assertThrowsDomainException(
                "User last name must have between 3 and 30 letters",
                () -> registerUserWithLastName(anInvalidNameWith2Letters));

        assertHasNoUsers(userRepository);
    }

    @Test
    void aUserLastNameCannotHaveMoreThan30Letters() {
        var anInvalidNameWith31Letters = "abcdefghijabcdefghijabcdefghijk";

        assertThrowsDomainException(
                "User last name must have between 3 and 30 letters",
                () -> registerUserWithLastName(anInvalidNameWith31Letters));

        assertHasNoUsers(userRepository);
    }

    @Test
    void aUserCannotHaveAnInvalidEmail() {
        var invalidEmail = "@invalid@email.com@";

        assertThrowsDomainException(
                "Invalid email format",
                () -> registerUserWithEmail(invalidEmail));

        assertHasNoUsers(userRepository);
    }

    @Test
    void aUserAddressCannotHaveLessThan10Characters() {
        var invalidShortAddress = "abc 12349";

        assertThrowsDomainException(
                "Address must have between 10 and 30 characters",
                () -> registerUserWithAddress(invalidShortAddress));

        assertHasNoUsers(userRepository);
    }

    @Test
    void aUserAddressCannotHaveMoreThan30Characters() {
        var invalidLongAddress = "a012345678901234567890123456789";

        assertThrowsDomainException(
                "Address must have between 10 and 30 characters",
                () -> registerUserWithAddress(invalidLongAddress));

        assertHasNoUsers(userRepository);
    }

    @Test
    void aUserCVUCannotHaveANumberOfDigitsDifferentTo22Digits() {
        var cvuNumberWithIncorrectLength = "012345678901234567890";

        assertThrowsDomainException(
                "Invalid CVU",
                () -> registerUserWithCVU(cvuNumberWithIncorrectLength));

        assertHasNoUsers(userRepository);
    }

    @Test
    void aUserCVUCannotContainAnyNonDigitCharacter() {
        var invalidCVUWithNonDigitCharacter = "012345678901234567890X";

        assertThrowsDomainException("Invalid CVU", () ->
                userService.registerUser(UserTestFactory.VALID_FIRST_NAME, UserTestFactory.VALID_LAST_NAME, UserTestFactory.VALID_EMAIL, UserTestFactory.VALID_ADDRESS, UserTestFactory.VALID_PASSWORD, invalidCVUWithNonDigitCharacter, UserTestFactory.VALID_CRIPTO_WALLET_ADDRESS));

        assertHasNoUsers(userRepository);
    }

    @Test
    void aUserCryptoWalletAddressCannotHaveANumberOfDigitsDifferentTo8Digits() {
        var cryptoWalletAddressWithIncorrectLength = "1234567";

        assertThrowsDomainException(
                "Invalid crypto wallet address",
                () -> registerUserWithCryptoWalletAddress(cryptoWalletAddressWithIncorrectLength));

        assertHasNoUsers(userRepository);
    }

    @Test
    void aUserCryptoWalletAddressCannotContainAnyNonDigitCharacter() {
        var cryptoWalletAddressWithNonDigitCharacter = "1234567x";

        assertThrowsDomainException(
                "Invalid crypto wallet address",
                () -> registerUserWithCryptoWalletAddress(cryptoWalletAddressWithNonDigitCharacter));

        assertHasNoUsers(userRepository);
    }

    private void assertHasUsers(UserRepository userRepository) {
        assertTrue(userRepository.count() > 0);
    }

    private void assertHasNoUsers(UserRepository userRepository) {
        assertEquals(0, userRepository.count());
    }

}
