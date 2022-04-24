package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import ar.edu.unq.desapp.grupod.backenddesappapi.factories.UserTestFactory;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.BuyOrder;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.CryptoAdvertisement;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.ModelException;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.User;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.CryptoAdvertisementsRepository;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.TradingOrdersRepository;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {

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
    CryptoAdvertisementsRepository cryptoAdvertisementsRepository;

    @Autowired
    TradingOrdersRepository tradingOrdersRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        cryptoAdvertisementsRepository.deleteAll();
        tradingOrdersRepository.deleteAll();
    }

    protected void assertAdvertisementHas(String cryptoActiveSymbol, int quantity, double price, User publisher, CryptoAdvertisement cryptoAdvertisement) {
        assertEquals(publisher.firstName(), cryptoAdvertisement.publisherFirstName());
        assertEquals(publisher.lastName(), cryptoAdvertisement.publisherLastName());
        assertEquals(cryptoActiveSymbol, cryptoAdvertisement.cryptoActiveSymbol());
        assertEquals(quantity, cryptoAdvertisement.quantity());
        assertEquals(price, cryptoAdvertisement.price());
    }

    protected void assertThrowsDomainExeption(String expectedErrorMessage, Executable executableToTest) {
        var error = assertThrows(ModelException.class, executableToTest);

        assertEquals(expectedErrorMessage, error.getMessage());
    }

    protected void assertHasNoAdvertisementsFor(String cryptoActiveSymbol, CryptoAdvertisementsRepository tradeAdvertisementRepository) {
        assertFalse(tradeAdvertisementRepository.existsByCryptoActiveSymbol(cryptoActiveSymbol));
    }

    protected User registerUser() {
        return userService.registerUser(UserTestFactory.VALID_FIRST_NAME, UserTestFactory.VALID_LAST_NAME, UserTestFactory.VALID_EMAIL, UserTestFactory.VALID_ADDRESS, UserTestFactory.VALID_PASSWORD, UserTestFactory.VALID_CVU, UserTestFactory.VALID_CRIPTO_WALLET_ADDRESS);
    }

    protected CryptoAdvertisement publishSellAdverticementFor(User aSeller, Integer quantityToSell) {
        return tradeService.postSellAdvertisement(aSeller.id(), CRYPTO_ACTIVE_SYMBOL, quantityToSell, VALID_ADVERTISEMENT_PRICE);
    }

    protected CryptoAdvertisement publishSellAdverticementFor(User aSeller) {
        return publishSellAdverticementFor(aSeller, VALID_ADVERTISEMENT_QUANTITY);
    }

    protected CryptoAdvertisement publishBuyAdverticementFor(User aBuyer) {
        return publishBuyAdverticementFor(aBuyer, VALID_ADVERTISEMENT_QUANTITY);
    }

    protected CryptoAdvertisement publishBuyAdverticementFor(User aSeller, Integer quantityToSell) {
        return tradeService.postBuyAdvertisement(aSeller.id(), CRYPTO_ACTIVE_SYMBOL, quantityToSell, VALID_ADVERTISEMENT_PRICE);
    }

    protected User registerJuan() {
        return userService.registerUser(UserTestFactory.JUAN_FIRST_NAME, UserTestFactory.JUAN_LAST_NAME, UserTestFactory.JUAN_EMAIL, UserTestFactory.JUAN_ADDRESS, UserTestFactory.JUAN_PASSWORD, UserTestFactory.JUAN_CVU, UserTestFactory.JUAN_CRIPTO_WALLET_ADDRESS);
    }

    protected User registerPepe() {
        return userService.registerUser(UserTestFactory.PEPE_FIRST_NAME, UserTestFactory.PEPE_LAST_NAME, UserTestFactory.PEPE_EMAIL, UserTestFactory.PEPE_ADDRESS, UserTestFactory.PEPE_PASSWORD, UserTestFactory.PEPE_CVU, UserTestFactory.PEPE_CRIPTO_WALLET_ADDRESS);
    }

    protected User registerUserWithFirstName(String firstName) {
        return userService.registerUser(firstName, UserTestFactory.VALID_LAST_NAME, UserTestFactory.VALID_EMAIL, UserTestFactory.VALID_ADDRESS, UserTestFactory.VALID_PASSWORD, UserTestFactory.VALID_CVU, UserTestFactory.VALID_CRIPTO_WALLET_ADDRESS);
    }

    protected User registerUserWithLastName(String lastName) {
        return userService.registerUser(UserTestFactory.VALID_FIRST_NAME, lastName, UserTestFactory.VALID_EMAIL, UserTestFactory.VALID_ADDRESS, UserTestFactory.VALID_PASSWORD, UserTestFactory.VALID_CVU, UserTestFactory.VALID_CRIPTO_WALLET_ADDRESS);
    }

    protected User registerUserWithEmail(String email) {
        return userService.registerUser(UserTestFactory.VALID_FIRST_NAME, UserTestFactory.VALID_LAST_NAME, email, UserTestFactory.VALID_ADDRESS, UserTestFactory.VALID_PASSWORD, UserTestFactory.VALID_CVU, UserTestFactory.VALID_CRIPTO_WALLET_ADDRESS);
    }

    protected User registerUserWithAddress(String address) {
        return userService.registerUser(UserTestFactory.VALID_FIRST_NAME, UserTestFactory.VALID_LAST_NAME, UserTestFactory.VALID_EMAIL, address, UserTestFactory.VALID_PASSWORD, UserTestFactory.VALID_CVU, UserTestFactory.VALID_CRIPTO_WALLET_ADDRESS);
    }

    protected User registerUserWithCVU(String cvu) {
        return userService.registerUser(UserTestFactory.VALID_FIRST_NAME, UserTestFactory.VALID_LAST_NAME, UserTestFactory.VALID_EMAIL, UserTestFactory.VALID_ADDRESS, UserTestFactory.VALID_PASSWORD, cvu, UserTestFactory.VALID_CRIPTO_WALLET_ADDRESS);
    }

    protected User registerUserWithCryptoWalletAddress(String cryptoWalletAddress) {
        return userService.registerUser(UserTestFactory.VALID_FIRST_NAME, UserTestFactory.VALID_LAST_NAME, UserTestFactory.VALID_EMAIL, UserTestFactory.VALID_ADDRESS, UserTestFactory.VALID_PASSWORD, UserTestFactory.VALID_CVU, cryptoWalletAddress);
    }

    protected void assertHasNoOrders(User aBuyer) {
        var orders = tradeService.ordersOf(aBuyer.id());
        assertTrue(orders.isEmpty());
    }

    protected void assertIsPendingOrderWith(User aBuyer, String symbolToBuy, int quantity, BuyOrder anOrder) {
        assertTrue(anOrder.wasPlaceBy(aBuyer));
        assertEquals(symbolToBuy, anOrder.symbol());
        assertEquals(quantity, anOrder.quantity());
        assertTrue(anOrder.isPending());
    }
}
