package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import ar.edu.unq.desapp.grupod.backenddesappapi.factories.UserTestFactory;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.CryptoAdvertisement;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.ModelException;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.User;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.CryptoAdvertisementsRepository;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.TradingOrdersRepository;
import ar.edu.unq.desapp.grupod.backenddesappapi.persistence.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class P2PSellTest {

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

    @Test
    void aUserCanPlaceABuyOrderForASellAdvertisementPublishedByAnotherUser() {
        var aBuyer = registerPepe();
        var aSeller = registerJuan();
        var aSellAdverticement = publishSellAdverticementFor(aSeller);

        var aSellOrder = tradeService.placeBuyOrder(aBuyer.id(), aSellAdverticement.id(), aSellAdverticement.quantity());

        assertTrue(aSellOrder.wasPlaceBy(aSeller));
        assertTrue(aSellOrder.isAssetOwner(aSeller));
    }

    @Test
    void whenASellerConfirmsASellOrderToBuySomeQuantityFromAnAdvertisementItsAvailableQuantityIsDecreased() {
        var aBuyer = registerPepe();
        var aSeller = registerJuan();

        var originallQuantityToSell = 3;
        var quantityToBuy = 1;
        var aSellAdverticement = publishSellAdverticementFor(aSeller, originallQuantityToSell);
        var aBuyOrder = tradeService.placeBuyOrder(aBuyer.id(), aSellAdverticement.id(), quantityToBuy);

        tradeService.confirmSuccessfulSell(aSeller.id(), aBuyOrder.id());

        var foundSellAdvertisements = tradeService.findSellAdvertisementsWithSymbol(aSellAdverticement.cryptoActiveSymbol());
        assertEquals(1, foundSellAdvertisements.size());
        assertEquals(originallQuantityToSell - quantityToBuy, foundSellAdvertisements.get(0).quantity());
    }

    @Test
    void whenASellerConfirmsASellOrderToBuyAllAvailableQuantityFromAnAdvertisementItIsRemovedFromTheListOfPublishedAdvertisements() {
        var aBuyer = registerPepe();
        var aSeller = registerJuan();

        var aSellAdverticement = publishSellAdverticementFor(aSeller);
        var aBuyOrder = tradeService.placeBuyOrder(aBuyer.id(), aSellAdverticement.id(), aSellAdverticement.quantity());

        tradeService.confirmSuccessfulSell(aSeller.id(), aBuyOrder.id());

        var foundSellAdvertisements = tradeService.findSellAdvertisementsWithSymbol(aSellAdverticement.cryptoActiveSymbol());
        assertTrue(foundSellAdvertisements.isEmpty());
    }

    @Test
    void aUserCannotPlaceABuyOrderForASellAdvertisementPublishedByHimself() {
        var aUser = registerPepe();

        var aSellAdverticement = publishSellAdverticementFor(aUser);

        assertThrowsDomainExeption(
                "Buyer and seller can't be the same user",
                () -> tradeService.placeBuyOrder(aUser.id(), aSellAdverticement.id(), aSellAdverticement.quantity())
        );
    }

    @Test
    void aUserCannotPlaceAnOrderWithAQuantityLesserThanOne() {
        var aBuyer = registerPepe();
        var aSeller = registerJuan();

        var aSellAdverticement = publishSellAdverticementFor(aSeller);

        var invalidQuantityLesserThanOne = 0;

        assertThrowsDomainExeption(
                "Quantity to buy must be greater than zero",
                () -> tradeService.placeBuyOrder(aBuyer.id(), aSellAdverticement.id(), invalidQuantityLesserThanOne)
        );
    }

    @Test
    void aBuyOrderCannotBePlacedWithANotRegisteredBuyerId() {
        var aSeller = registerJuan();
        var aSellAdverticement = publishSellAdverticementFor(aSeller);

        var notRegisteredBuyerId = 123L;

        assertThrowsDomainExeption(
                "User not found",
                () -> tradeService.placeBuyOrder(notRegisteredBuyerId, aSellAdverticement.id(), aSellAdverticement.quantity())
        );
    }

    @Test
    void aBuyOrderCannotBePlacedWithANotRegisteredSellAdvertisementId() {
        var aBuyer = registerPepe();
        var quantityToBuy = 1;

        var notRegisterdSellAdvertisementId = 123L;

        assertThrowsDomainExeption(
                "Adverticement not found",
                () -> tradeService.placeBuyOrder(aBuyer.id(), notRegisterdSellAdvertisementId, quantityToBuy)
        );
    }

    @Test
    void aUserCannotConfirmABuyOrderPlacedByHimself() {
        var aBuyer = registerPepe();
        var aSeller = registerJuan();

        var aSellAdverticement = publishSellAdverticementFor(aSeller);
        var aBuyOrder = tradeService.placeBuyOrder(aBuyer.id(), aSellAdverticement.id(), aSellAdverticement.quantity());

        assertThrowsDomainExeption(
                "A user cannot confirm an order placed by himself",
                () -> tradeService.confirmSuccessfulSell(aBuyer.id(), aBuyOrder.id())
        );
    }

    private void assertThrowsDomainExeption(String expectedErrorMessage, Executable executableToTest) {
        var error = assertThrows(ModelException.class, executableToTest);

        assertEquals(expectedErrorMessage, error.getMessage());
    }

    private CryptoAdvertisement publishSellAdverticementFor(User aBuyer, Integer quantityToSell) {
        return tradeService.postSellAdvertisement(aBuyer.id(), CRYPTO_ACTIVE_SYMBOL, quantityToSell, VALID_ADVERTISEMENT_PRICE);
    }

    private CryptoAdvertisement publishSellAdverticementFor(User aBuyer) {
        return publishSellAdverticementFor(aBuyer, VALID_ADVERTISEMENT_QUANTITY);
    }

    private User registerJuan() {
        return userService.registerUser(UserTestFactory.JUAN_FIRST_NAME, UserTestFactory.JUAN_LAST_NAME, UserTestFactory.JUAN_EMAIL, UserTestFactory.JUAN_ADDRESS, UserTestFactory.JUAN_PASSWORD, UserTestFactory.JUAN_CVU, UserTestFactory.JUAN_CRIPTO_WALLET_ADDRESS);
    }

    private User registerPepe() {
        return userService.registerUser(UserTestFactory.PEPE_FIRST_NAME, UserTestFactory.PEPE_LAST_NAME, UserTestFactory.PEPE_EMAIL, UserTestFactory.PEPE_ADDRESS, UserTestFactory.PEPE_PASSWORD, UserTestFactory.PEPE_CVU, UserTestFactory.PEPE_CRIPTO_WALLET_ADDRESS);
    }

}