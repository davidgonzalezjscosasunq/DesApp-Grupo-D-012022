package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserRegistrationTest {

    public static final String VALID_FIRST_NAME = "Pepe";
    public static final String VALID_LAST_NAME = "Ramirez";
    public static final String VALID_EMAIL = "pepe@domain.com";
    public static final String VALID_ADDRESS = "Some Address 123";
    public static final String VALID_PASSWORD = "password";
    public static final String VALID_CVU = "0123456789012345678912";
    public static final String VALID_CRIPTO_WALLET_ADDRESS = "12345678";

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
        var user = userService.registerUser(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_EMAIL, VALID_ADDRESS, VALID_PASSWORD, VALID_CVU, VALID_CRIPTO_WALLET_ADDRESS);

        assertTrue(userService.hasUserRegisteredWithEmail(VALID_EMAIL));
    }

    @Test
    void aUserFirstNameCannotHaveLessThan3Letters() {
        var anInvalidNameWith2Letters = "ab";

        var error = assertThrows(
                RuntimeException.class, () ->
                userService.registerUser(anInvalidNameWith2Letters, VALID_LAST_NAME, VALID_EMAIL, VALID_ADDRESS, VALID_PASSWORD, VALID_CVU, VALID_CRIPTO_WALLET_ADDRESS)
        );

        assertEquals("User first name must have between 3 and 30 letters", error.getMessage());
        assertFalse(userService.hasUserRegisteredWithEmail(VALID_EMAIL));
    }

    @Test
    void aUserFirstNameCannotHaveMoreThan30Letters() {
        var anInvalidNameWith31Letters = "abcdefghijabcdefghijabcdefghijk";

        var error = assertThrows(
                RuntimeException.class, () ->
                        userService.registerUser(anInvalidNameWith31Letters, VALID_LAST_NAME, VALID_EMAIL, VALID_ADDRESS, VALID_PASSWORD, VALID_CVU, VALID_CRIPTO_WALLET_ADDRESS)
        );

        assertEquals("User first name must have between 3 and 30 letters", error.getMessage());
        assertFalse(userService.hasUserRegisteredWithEmail(VALID_EMAIL));
    }

    @Test
    void aUserLastNameCannotHaveLessThan3Letters() {
        var anInvalidNameWith2Letters = "ab";

        var error = assertThrows(
                RuntimeException.class, () ->
                        userService.registerUser(VALID_FIRST_NAME, anInvalidNameWith2Letters, VALID_EMAIL, VALID_ADDRESS, VALID_PASSWORD, VALID_CVU, VALID_CRIPTO_WALLET_ADDRESS)
        );

        assertEquals("User last name must have between 3 and 30 letters", error.getMessage());
        assertFalse(userService.hasUserRegisteredWithEmail(VALID_EMAIL));
    }

    @Test
    void aUserLastNameCannotHaveMoreThan30Letters() {
        var anInvalidNameWith31Letters = "abcdefghijabcdefghijabcdefghijk";

        var error = assertThrows(
                RuntimeException.class, () ->
                        userService.registerUser(VALID_FIRST_NAME, anInvalidNameWith31Letters, VALID_EMAIL, VALID_ADDRESS, VALID_PASSWORD, VALID_CVU, VALID_CRIPTO_WALLET_ADDRESS)
        );

        assertEquals("User last name must have between 3 and 30 letters", error.getMessage());
        assertFalse(userService.hasUserRegisteredWithEmail(VALID_EMAIL));
    }

    @Test
    void aUserCannotHaveAnInvalidEmail() {
        var invalidEmail = "@invalid@email.com@";

        var error = assertThrows(
                RuntimeException.class, () ->
                        userService.registerUser(VALID_FIRST_NAME, VALID_LAST_NAME, invalidEmail, VALID_ADDRESS, VALID_PASSWORD, VALID_CVU, VALID_CRIPTO_WALLET_ADDRESS)
        );

        assertEquals("Invalid email format", error.getMessage());
        assertFalse(userService.hasUserRegisteredWithEmail(invalidEmail));
    }

    @Test
    void aUserAddressCannotHaveLessThan10Characters() {
        var invalidShortAddress = "abc 12349";

        var error = assertThrows(
                RuntimeException.class, () ->
                        userService.registerUser(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_EMAIL, invalidShortAddress, VALID_PASSWORD, VALID_CVU, VALID_CRIPTO_WALLET_ADDRESS)
        );

        assertEquals("Address must have between 10 and 30 characters", error.getMessage());
        assertFalse(userService.hasUserRegisteredWithEmail(VALID_EMAIL));
    }

    @Test
    void aUserAddressCannotHaveMoreThan30Characters() {
        var invalidShortAddress = "a012345678901234567890123456789";

        var error = assertThrows(
                RuntimeException.class, () ->
                        userService.registerUser(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_EMAIL, invalidShortAddress, VALID_PASSWORD, VALID_CVU, VALID_CRIPTO_WALLET_ADDRESS)
        );

        assertEquals("Address must have between 10 and 30 characters", error.getMessage());
        assertFalse(userService.hasUserRegisteredWithEmail(VALID_EMAIL));
    }

    @Test
    void aUserCVUCannotHaveANumberOfDigitsDifferentTo22Digits() {
        var cvuNumberWithIncorrectLength = "012345678901234567890";

        var error = assertThrows(
                RuntimeException.class, () ->
                        userService.registerUser(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_EMAIL, VALID_ADDRESS, VALID_PASSWORD, cvuNumberWithIncorrectLength, VALID_CRIPTO_WALLET_ADDRESS)
        );

        assertEquals("Invalid CVU", error.getMessage());
        assertFalse(userService.hasUserRegisteredWithEmail(VALID_EMAIL));
    }

    @Test
    void aUserCVUCannotContainAnyNonDigitCharacter() {
        var invalidCVUWithNonDigitCharacter = "012345678901234567890X";

        var error = assertThrows(
                RuntimeException.class, () ->
                        userService.registerUser(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_EMAIL, VALID_ADDRESS, VALID_PASSWORD, invalidCVUWithNonDigitCharacter, VALID_CRIPTO_WALLET_ADDRESS)
        );

        assertEquals("Invalid CVU", error.getMessage());
        assertFalse(userService.hasUserRegisteredWithEmail(VALID_EMAIL));
    }

    @Test
    void aUserCryptoWalletAddressCannotHaveANumberOfDigitsDifferentTo8Digits() {
        var cryptoWalletAddressWithIncorrectLength = "1234567";

        var error = assertThrows(
                RuntimeException.class, () ->
                        userService.registerUser(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_EMAIL, VALID_ADDRESS, VALID_PASSWORD, VALID_CVU, cryptoWalletAddressWithIncorrectLength)
        );

        assertEquals("Invalid crypto wallet address", error.getMessage());
        assertFalse(userService.hasUserRegisteredWithEmail(VALID_EMAIL));
    }

    @Test
    void aUserCryptoWalletAddressCannotContainAnyNonDigitCharacter() {
        var cryptoWalletAddressWithNonDigitCharacter = "1234567x";

        var error = assertThrows(
                RuntimeException.class, () ->
                        userService.registerUser(VALID_FIRST_NAME, VALID_LAST_NAME, VALID_EMAIL, VALID_ADDRESS, VALID_PASSWORD, VALID_CVU, cryptoWalletAddressWithNonDigitCharacter)
        );

        assertEquals("Invalid crypto wallet address", error.getMessage());
        assertFalse(userService.hasUserRegisteredWithEmail(VALID_EMAIL));
    }

}
