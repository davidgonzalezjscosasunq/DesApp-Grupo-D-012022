package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.ModelException;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.User;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserRegistrationTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void aUserCanBeRegistered() {
        var user = userService.registerUser(UserTestFactory.VALID_FIRST_NAME, UserTestFactory.VALID_LAST_NAME, UserTestFactory.VALID_EMAIL, UserTestFactory.VALID_ADDRESS, UserTestFactory.VALID_PASSWORD, UserTestFactory.VALID_CVU, UserTestFactory.VALID_CRIPTO_WALLET_ADDRESS);

        assertTrue(userService.hasUserRegisteredWithEmail(UserTestFactory.VALID_EMAIL));
    }

    @Test
    void aUserFirstNameCannotHaveLessThan3Letters() {
        var anInvalidNameWith2Letters = "ab";

        assertThrowsDomainExeption(
                "User first name must have between 3 and 30 letters",
                () -> registerUserWithFirstName(anInvalidNameWith2Letters));

        assertHasNoUserRegisteredWithEmail(UserTestFactory.VALID_EMAIL);
    }

    @Test
    void aUserFirstNameCannotHaveMoreThan30Letters() {
        var anInvalidNameWith31Letters = "abcdefghijabcdefghijabcdefghijk";

        assertThrowsDomainExeption("User first name must have between 3 and 30 letters",
                () -> registerUserWithFirstName(anInvalidNameWith31Letters));

        assertHasNoUserRegisteredWithEmail(UserTestFactory.VALID_EMAIL);
    }

    @Test
    void aUserLastNameCannotHaveLessThan3Letters() {
        var anInvalidNameWith2Letters = "ab";

        assertThrowsDomainExeption(
                "User last name must have between 3 and 30 letters",
                () -> registerUserWithLastName(anInvalidNameWith2Letters));

        assertHasNoUserRegisteredWithEmail(UserTestFactory.VALID_EMAIL);
    }

    @Test
    void aUserLastNameCannotHaveMoreThan30Letters() {
        var anInvalidNameWith31Letters = "abcdefghijabcdefghijabcdefghijk";

        assertThrowsDomainExeption(
                "User last name must have between 3 and 30 letters",
                () -> registerUserWithLastName(anInvalidNameWith31Letters));

        assertHasNoUserRegisteredWithEmail(UserTestFactory.VALID_EMAIL);
    }

    @Test
    void aUserCannotHaveAnInvalidEmail() {
        var invalidEmail = "@invalid@email.com@";

        assertThrowsDomainExeption(
                "Invalid email format",
                () -> registerUserWithEmail(invalidEmail));

        assertHasNoUserRegisteredWithEmail(invalidEmail);
    }

    @Test
    void aUserAddressCannotHaveLessThan10Characters() {
        var invalidShortAddress = "abc 12349";

        assertThrowsDomainExeption(
                "Address must have between 10 and 30 characters",
                () -> registerUserWithAddress(invalidShortAddress));

        assertHasNoUserRegisteredWithEmail(UserTestFactory.VALID_EMAIL);
    }

    @Test
    void aUserAddressCannotHaveMoreThan30Characters() {
        var invalidLongAddress = "a012345678901234567890123456789";

        assertThrowsDomainExeption(
                "Address must have between 10 and 30 characters",
                () -> registerUserWithAddress(invalidLongAddress));

        assertHasNoUserRegisteredWithEmail(UserTestFactory.VALID_EMAIL);
    }

    @Test
    void aUserCVUCannotHaveANumberOfDigitsDifferentTo22Digits() {
        var cvuNumberWithIncorrectLength = "012345678901234567890";

        assertThrowsDomainExeption(
                "Invalid CVU",
                () -> registerUserWithCVU(cvuNumberWithIncorrectLength));

        assertHasNoUserRegisteredWithEmail(UserTestFactory.VALID_EMAIL);
    }

    @Test
    void aUserCVUCannotContainAnyNonDigitCharacter() {
        var invalidCVUWithNonDigitCharacter = "012345678901234567890X";

        assertThrowsDomainExeption("Invalid CVU", () ->
                userService.registerUser(UserTestFactory.VALID_FIRST_NAME, UserTestFactory.VALID_LAST_NAME, UserTestFactory.VALID_EMAIL, UserTestFactory.VALID_ADDRESS, UserTestFactory.VALID_PASSWORD, invalidCVUWithNonDigitCharacter, UserTestFactory.VALID_CRIPTO_WALLET_ADDRESS));

        assertHasNoUserRegisteredWithEmail(UserTestFactory.VALID_EMAIL);
    }

    @Test
    void aUserCryptoWalletAddressCannotHaveANumberOfDigitsDifferentTo8Digits() {
        var cryptoWalletAddressWithIncorrectLength = "1234567";

        assertThrowsDomainExeption(
                "Invalid crypto wallet address",
                () -> registerUserWithCryptoWalletAddress(cryptoWalletAddressWithIncorrectLength));

        assertHasNoUserRegisteredWithEmail(UserTestFactory.VALID_EMAIL);
    }

    @Test
    void aUserCryptoWalletAddressCannotContainAnyNonDigitCharacter() {
        var cryptoWalletAddressWithNonDigitCharacter = "1234567x";

        assertThrowsDomainExeption(
                "Invalid crypto wallet address",
                () -> registerUserWithCryptoWalletAddress(cryptoWalletAddressWithNonDigitCharacter));

        assertHasNoUserRegisteredWithEmail(UserTestFactory.VALID_EMAIL);
    }

    private void assertHasNoUserRegisteredWithEmail(String email) {
        assertFalse(userService.hasUserRegisteredWithEmail(email));
    }

    private void assertThrowsDomainExeption(String expectedErrorMessage, Executable executableToTest) {
        var error = assertThrows(ModelException.class, executableToTest);

        assertEquals(expectedErrorMessage, error.getMessage());
    }

    private User registerUserWithFirstName(String firstName) {
        return userService.registerUser(firstName, UserTestFactory.VALID_LAST_NAME, UserTestFactory.VALID_EMAIL, UserTestFactory.VALID_ADDRESS, UserTestFactory.VALID_PASSWORD, UserTestFactory.VALID_CVU, UserTestFactory.VALID_CRIPTO_WALLET_ADDRESS);
    }

    private User registerUserWithLastName(String lastName) {
        return userService.registerUser(UserTestFactory.VALID_FIRST_NAME, lastName, UserTestFactory.VALID_EMAIL, UserTestFactory.VALID_ADDRESS, UserTestFactory.VALID_PASSWORD, UserTestFactory.VALID_CVU, UserTestFactory.VALID_CRIPTO_WALLET_ADDRESS);
    }

    private User registerUserWithEmail(String email) {
        return userService.registerUser(UserTestFactory.VALID_FIRST_NAME, UserTestFactory.VALID_LAST_NAME, email, UserTestFactory.VALID_ADDRESS, UserTestFactory.VALID_PASSWORD, UserTestFactory.VALID_CVU, UserTestFactory.VALID_CRIPTO_WALLET_ADDRESS);
    }

    private User registerUserWithAddress(String address) {
        return userService.registerUser(UserTestFactory.VALID_FIRST_NAME, UserTestFactory.VALID_LAST_NAME, UserTestFactory.VALID_EMAIL, address, UserTestFactory.VALID_PASSWORD, UserTestFactory.VALID_CVU, UserTestFactory.VALID_CRIPTO_WALLET_ADDRESS);
    }

    private User registerUserWithCVU(String cvu) {
        return userService.registerUser(UserTestFactory.VALID_FIRST_NAME, UserTestFactory.VALID_LAST_NAME, UserTestFactory.VALID_EMAIL, UserTestFactory.VALID_ADDRESS, UserTestFactory.VALID_PASSWORD, cvu, UserTestFactory.VALID_CRIPTO_WALLET_ADDRESS);
    }

    private User registerUserWithCryptoWalletAddress(String cryptoWalletAddress) {
        return userService.registerUser(UserTestFactory.VALID_FIRST_NAME, UserTestFactory.VALID_LAST_NAME, UserTestFactory.VALID_EMAIL, UserTestFactory.VALID_ADDRESS, UserTestFactory.VALID_PASSWORD, UserTestFactory.VALID_CVU, cryptoWalletAddress);
    }

}
