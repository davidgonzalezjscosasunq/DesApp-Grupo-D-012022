package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import ar.edu.unq.desapp.grupod.backenddesappapi.factories.UserTestFactory;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.ModelException;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.CryptoAdvertisement;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.User;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.CryptoAdvertisementsRepository;
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
    public static final double VALID_ADVERTISEMENT_PRICE = 2.0;
    public static final int VALID_ADVERTISEMENT_QUANTITY = 1;
    public static final double ZERO_PESOS = 0.0;
    public static final double TWENTY_PESOS = 20.0;
    public static final double FOURTY_PESOS = 40.0;

    @Autowired
    TradeService tradeService;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CryptoAdvertisementsRepository tradeAdvertisementRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        tradeAdvertisementRepository.deleteAll();
    }

    @Test
    void aBuyAdvertisementCanBePostedByARegisteredUser() {
        var buyer = registerUser();

        var buyAdvertisement = tradeService.postBuyAdvertisement(buyer.id(), CRYPTO_ACTIVE_SYMBOL, VALID_ADVERTISEMENT_QUANTITY, VALID_ADVERTISEMENT_PRICE);

        assertAdvertisementHas(CRYPTO_ACTIVE_SYMBOL, VALID_ADVERTISEMENT_QUANTITY, VALID_ADVERTISEMENT_PRICE, buyer, buyAdvertisement);
    }

    @Test
    void aSellAdvertisementCanBePostedByARegisteredUser() {
        var seller = registerUser();

        var sellAdvertisement = tradeService.postSellAdvertisement(seller.id(), CRYPTO_ACTIVE_SYMBOL, VALID_ADVERTISEMENT_QUANTITY, VALID_ADVERTISEMENT_PRICE);

        assertAdvertisementHas(CRYPTO_ACTIVE_SYMBOL, VALID_ADVERTISEMENT_QUANTITY, VALID_ADVERTISEMENT_PRICE, seller, sellAdvertisement);
    }

    @Test
    void postedSellAdvertisementsCanBeSearchByCryptoActiveSymbol() {
        var seller = registerUser();

        tradeService.postSellAdvertisement(seller.id(), CRYPTO_ACTIVE_SYMBOL, VALID_ADVERTISEMENT_QUANTITY, VALID_ADVERTISEMENT_PRICE);

        var foundSellAdvertisements = tradeService.findSellAdvertisementsWithSymbol(CRYPTO_ACTIVE_SYMBOL);

        assertEquals(1, foundSellAdvertisements.size());
        assertAdvertisementHas(CRYPTO_ACTIVE_SYMBOL, VALID_ADVERTISEMENT_QUANTITY, VALID_ADVERTISEMENT_PRICE, seller, foundSellAdvertisements.get(0));
    }

    @Test
    void postedSellAdvertisementsSearchResultsDoesNotIncludeBuyAdvertisementPosts() {
        var publisher = registerUser();
        var buyPrice = TWENTY_PESOS;
        var sellPrice = FOURTY_PESOS;
        tradeService.postBuyAdvertisement(publisher.id(), CRYPTO_ACTIVE_SYMBOL, VALID_ADVERTISEMENT_QUANTITY, buyPrice);
        tradeService.postSellAdvertisement(publisher.id(), CRYPTO_ACTIVE_SYMBOL, VALID_ADVERTISEMENT_QUANTITY, sellPrice);

        var foundSellAdvertisements = tradeService.findSellAdvertisementsWithSymbol(CRYPTO_ACTIVE_SYMBOL);

        assertEquals(1, foundSellAdvertisements.size());
        assertEquals(sellPrice, foundSellAdvertisements.get(0).price());
    }

    @Test
    void postedBuyAdvertisementsSearchResultsDoesNotIncludeSellAdvertisementPosts() {
        var publisher = registerUser();
        var buyPrice = TWENTY_PESOS;
        var sellPrice = FOURTY_PESOS;
        tradeService.postBuyAdvertisement(publisher.id(), CRYPTO_ACTIVE_SYMBOL, VALID_ADVERTISEMENT_QUANTITY, buyPrice);
        tradeService.postSellAdvertisement(publisher.id(), CRYPTO_ACTIVE_SYMBOL, VALID_ADVERTISEMENT_QUANTITY, sellPrice);

        var foundSellAdvertisements = tradeService.findBuyAdvertisementsWithSymbol(CRYPTO_ACTIVE_SYMBOL);

        assertEquals(1, foundSellAdvertisements.size());
        assertEquals(buyPrice, foundSellAdvertisements.get(0).price());
    }

    @Test
    void aSellAdvertisementCannotBePostedForANotRegisteredUser() {
        var notRegisteredUserId = 123L;

        assertThrowsDomainExeption(
                "User not found",
                () -> tradeService.postSellAdvertisement(notRegisteredUserId, CRYPTO_ACTIVE_SYMBOL, VALID_ADVERTISEMENT_QUANTITY, VALID_ADVERTISEMENT_PRICE));

        assertHasNoAdvertisementsFor(CRYPTO_ACTIVE_SYMBOL, tradeAdvertisementRepository);
    }

    @Test
    void aSellAdvertisementCannotBePostedWithAQuantityLesserThanOne() {
        var seller = registerUser();
        var invalidQuantity = 0;

        assertThrowsDomainExeption(
                "Quantity cannot be lesser than 1",
                () -> tradeService.postSellAdvertisement(seller.id(), CRYPTO_ACTIVE_SYMBOL, invalidQuantity, VALID_ADVERTISEMENT_PRICE));

        assertHasNoAdvertisementsFor(CRYPTO_ACTIVE_SYMBOL, tradeAdvertisementRepository);
    }

    @Test
    void aSellAdvertisementCannotBePostedWithANonPositiveAmountOfMoney() {
        var seller = registerUser();
        var invalidPrice = ZERO_PESOS;

        assertThrowsDomainExeption(
                "Price amount of money must be positive",
                () -> tradeService.postSellAdvertisement(seller.id(), CRYPTO_ACTIVE_SYMBOL, VALID_ADVERTISEMENT_QUANTITY, invalidPrice));

        assertHasNoAdvertisementsFor(CRYPTO_ACTIVE_SYMBOL, tradeAdvertisementRepository);
    }

    private void assertAdvertisementHas(String cryptoActiveSymbol, int quantity, double price, User publisher, CryptoAdvertisement cryptoAdvertisement) {
        assertEquals(publisher.firstName(), cryptoAdvertisement.publisherFirstName());
        assertEquals(publisher.lastName(), cryptoAdvertisement.publisherLastName());
        assertEquals(cryptoActiveSymbol, cryptoAdvertisement.cryptoActiveSymbol());
        assertEquals(quantity, cryptoAdvertisement.quantity());
        assertEquals(price, cryptoAdvertisement.price());
    }

    private void assertThrowsDomainExeption(String expectedErrorMessage, Executable executableToTest) {
        var error = assertThrows(ModelException.class, executableToTest);

        assertEquals(expectedErrorMessage, error.getMessage());
    }

    private void assertHasNoAdvertisementsFor(String cryptoActiveSymbol, CryptoAdvertisementsRepository tradeAdvertisementRepository) {
        assertFalse(tradeAdvertisementRepository.existsByCryptoActiveSymbol(cryptoActiveSymbol));
    }

    private User registerUser() {
        return userService.registerUser(UserTestFactory.VALID_FIRST_NAME, UserTestFactory.VALID_LAST_NAME, UserTestFactory.VALID_EMAIL, UserTestFactory.VALID_ADDRESS, UserTestFactory.VALID_PASSWORD, UserTestFactory.VALID_CVU, UserTestFactory.VALID_CRIPTO_WALLET_ADDRESS);
    }

}
