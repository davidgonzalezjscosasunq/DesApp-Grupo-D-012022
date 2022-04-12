package ar.edu.unq.desapp.grupod.backenddesappapi.service;

public class UserTestFactory {
    public static final String VALID_FIRST_NAME = "Pepe";
    public static final String VALID_LAST_NAME = "Ramirez";
    public static final String VALID_EMAIL = "pepe@domain.com";
    public static final String VALID_ADDRESS = "Some Address 123";
    public static final String VALID_PASSWORD = "password";
    public static final String VALID_CVU = "0123456789012345678912";
    public static final String VALID_CRIPTO_WALLET_ADDRESS = "12345678";

    public static String emailFor(String firstName, String lastName) {
        return firstName + lastName + "@domain.com";
    }
}