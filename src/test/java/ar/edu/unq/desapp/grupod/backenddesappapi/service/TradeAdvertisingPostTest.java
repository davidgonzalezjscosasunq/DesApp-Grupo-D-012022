package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.ModelException;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.SellAdvertisement;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.User;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.TradeAdvertisementRepository;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class TradeAdvertisingPostTest {

    public static final String CRYPTO_ACTIVE_SYMBOL = "NEOUSDT";
    public static final double VALID_SELLING_PRICE = 2.0;
    public static final int VALID_QUANTITY_TO_SELL = 1;
    public static final double ZERO_PESOS = 0.0;

    @Autowired
    TradeService tradeService;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TradeAdvertisementRepository tradeAdvertisementRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        tradeAdvertisementRepository.deleteAll();
    }

    @Test
    void aSellAdvertisementCanBePostedByARegisteredUser() {
        var seller = registerUser();

        var sellAdvertisement = tradeService.postSellAdvertisement(CRYPTO_ACTIVE_SYMBOL, VALID_QUANTITY_TO_SELL, VALID_SELLING_PRICE, seller.firstName(), seller.lastName());

        assertSellAdvertisementHas(CRYPTO_ACTIVE_SYMBOL, VALID_QUANTITY_TO_SELL, VALID_SELLING_PRICE, seller, sellAdvertisement);
    }

    @Test
    void postedSellAdvertisementsCanBeSearchByCryptoActiveSymbol() {
        var seller = registerUser();

        tradeService.postSellAdvertisement(CRYPTO_ACTIVE_SYMBOL, VALID_QUANTITY_TO_SELL, VALID_SELLING_PRICE, seller.firstName(), seller.lastName());

        var foundSellAdvertisements = tradeService.findSellAdvertisementsWithSymbol(CRYPTO_ACTIVE_SYMBOL);

        assertEquals(1, foundSellAdvertisements.size());
        assertSellAdvertisementHas(CRYPTO_ACTIVE_SYMBOL, VALID_QUANTITY_TO_SELL, VALID_SELLING_PRICE, seller, foundSellAdvertisements.get(0));
    }

    @Test
    void aSellAdvertisementCannotBePostedWithAFirstNameNotMatchingAnyUser() {
        var seller = registerUser();

        var notRegisteredUserFirstName = seller.firstName() + "abc";

        assertThrowsDomainExeption(
                "User not found",
                () -> tradeService.postSellAdvertisement(CRYPTO_ACTIVE_SYMBOL, VALID_QUANTITY_TO_SELL, VALID_SELLING_PRICE, notRegisteredUserFirstName, seller.lastName()));

        assertHasNoAdvertisementsFor(CRYPTO_ACTIVE_SYMBOL, tradeAdvertisementRepository);
    }

    @Test
    void aSellAdvertisementCannotBePostedWithALastNameNotMatchingAnyUser() {
        var seller = registerUser();

        var notRegisteredUserLastName = seller.lastName() + "abc";

        assertThrowsDomainExeption(
                "User not found",
                () -> tradeService.postSellAdvertisement(CRYPTO_ACTIVE_SYMBOL, VALID_QUANTITY_TO_SELL, VALID_SELLING_PRICE, seller.firstName(), notRegisteredUserLastName));

        assertHasNoAdvertisementsFor(CRYPTO_ACTIVE_SYMBOL, tradeAdvertisementRepository);
    }

    @Test
    void aSellAdvertisementCannotBePostedWithAQuantityLesserThanOne() {
        var seller = registerUser();
        var invalidQuantity = 0;

        assertThrowsDomainExeption(
                "Quantity cannot be lesser than 1",
                () -> tradeService.postSellAdvertisement(CRYPTO_ACTIVE_SYMBOL, invalidQuantity, VALID_SELLING_PRICE, seller.firstName(), seller.lastName()));

        assertHasNoAdvertisementsFor(CRYPTO_ACTIVE_SYMBOL, tradeAdvertisementRepository);
    }

    @Test
    void aSellAdvertisementCannotBePostedWithANonPositiveAmountOfMoney() {
        var seller = registerUser();
        var invalidPrice = ZERO_PESOS;

        assertThrowsDomainExeption(
                "Price amount of money must be positive",
                () -> tradeService.postSellAdvertisement(CRYPTO_ACTIVE_SYMBOL, VALID_QUANTITY_TO_SELL, invalidPrice, seller.firstName(), seller.lastName()));

        assertHasNoAdvertisementsFor(CRYPTO_ACTIVE_SYMBOL, tradeAdvertisementRepository);
    }

    private void assertSellAdvertisementHas(String cryptoActiveSymbol, int quantityToSell, double sellingPrice, User seller, SellAdvertisement sellAdvertisement) {
        assertEquals(seller.firstName(), sellAdvertisement.publisherFirstName());
        assertEquals(seller.lastName(), sellAdvertisement.publisherLastName());
        assertEquals(cryptoActiveSymbol, sellAdvertisement.cryptoActiveSymbol());
        assertEquals(quantityToSell, sellAdvertisement.quantity());
        assertEquals(sellingPrice, sellAdvertisement.price());
    }

    private void assertThrowsDomainExeption(String expectedErrorMessage, Executable executableToTest) {
        var error = assertThrows(ModelException.class, executableToTest);

        assertEquals(expectedErrorMessage, error.getMessage());
    }

    private void assertHasNoAdvertisementsFor(String cryptoActiveSymbol, TradeAdvertisementRepository tradeAdvertisementRepository) {
        assertFalse(tradeAdvertisementRepository.existsByCryptoActiveSymbol(cryptoActiveSymbol));
    }

    private User registerUser() {
        return userService.registerUser(UserTestFactory.VALID_FIRST_NAME, UserTestFactory.VALID_LAST_NAME, UserTestFactory.VALID_EMAIL, UserTestFactory.VALID_ADDRESS, UserTestFactory.VALID_PASSWORD, UserTestFactory.VALID_CVU, UserTestFactory.VALID_CRIPTO_WALLET_ADDRESS);
    }

}
