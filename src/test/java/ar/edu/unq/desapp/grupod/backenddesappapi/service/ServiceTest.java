package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import ar.edu.unq.desapp.grupod.backenddesappapi.factories.UserTestFactory;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.Transaction;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.AssetAdvertisement;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.ModelException;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.User;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.AssetAdvertisementsRepository;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.TransactionsRepository;
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
    TradingService tradingService;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AssetAdvertisementsRepository assetAdvertisementsRepository;

    @Autowired
    TransactionsRepository transactionsRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        assetAdvertisementsRepository.deleteAll();
        transactionsRepository.deleteAll();
    }

    protected void assertAdvertisementHas(String assetSymbol, int quantity, double price, User publisher, AssetAdvertisement cryptoAdvertisement) {
        assertEquals(publisher.firstName(), cryptoAdvertisement.publisherFirstName());
        assertEquals(publisher.lastName(), cryptoAdvertisement.publisherLastName());
        assertEquals(assetSymbol, cryptoAdvertisement.assetSymbol());
        assertEquals(quantity, cryptoAdvertisement.quantity());
        assertEquals(price, cryptoAdvertisement.price());
    }

    protected void assertThrowsDomainException(String expectedErrorMessage, Executable executableToTest) {
        var error = assertThrows(ModelException.class, executableToTest);

        assertEquals(expectedErrorMessage, error.getMessage());
    }

    protected void assertHasNoAdvertisementsFor(String assetSymbol, AssetAdvertisementsRepository tradeAdvertisementRepository) {
        assertFalse(tradeAdvertisementRepository.existsByAssetSymbol(assetSymbol));
    }

    protected User registerUser() {
        return userService.registerUser(UserTestFactory.VALID_FIRST_NAME, UserTestFactory.VALID_LAST_NAME, UserTestFactory.VALID_EMAIL, UserTestFactory.VALID_ADDRESS, UserTestFactory.VALID_PASSWORD, UserTestFactory.VALID_CVU, UserTestFactory.VALID_CRIPTO_WALLET_ADDRESS);
    }

    protected AssetAdvertisement publishSellAdvertisementFor(User aSeller, Integer quantityToSell) {
        return tradingService.postSellAdvertisement(aSeller.id(), CRYPTO_ACTIVE_SYMBOL, quantityToSell, VALID_ADVERTISEMENT_PRICE);
    }

    protected AssetAdvertisement publishSellAdvertisementFor(User aSeller) {
        return publishSellAdvertisementFor(aSeller, VALID_ADVERTISEMENT_QUANTITY);
    }

    protected AssetAdvertisement publishBuyAdvertisementFor(User aBuyer) {
        return publishBuyAdvertisementFor(aBuyer, VALID_ADVERTISEMENT_QUANTITY);
    }

    protected AssetAdvertisement publishBuyAdvertisementFor(User aSeller, Integer quantityToSell) {
        return tradingService.postBuyAdvertisement(aSeller.id(), CRYPTO_ACTIVE_SYMBOL, quantityToSell, VALID_ADVERTISEMENT_PRICE);
    }

    protected AssetAdvertisement publishAdvertisementFor(User publisher) {
        return publishSellAdvertisementFor(publisher);
    }

    protected AssetAdvertisement publishAdvertisementFor(User publisher, Integer quantity) {
        return publishSellAdvertisementFor(publisher, quantity);
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

    protected void assertHasNoInformedTransactions(User aBuyer) {
        var orders = tradingService.findTransactionsInformedBy(aBuyer.id());
        assertTrue(orders.isEmpty());
    }

    protected void assertIsPendingTransactionWith(User aBuyer, String symbolToBuy, int quantity, Transaction anOrder) {
        assertTrue(anOrder.wasInformedBy(aBuyer));
        assertEquals(symbolToBuy, anOrder.symbol());
        assertEquals(quantity, anOrder.quantity());
        assertTrue(anOrder.isPending());
    }
}
