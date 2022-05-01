package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AdvertisementPostingTest extends ServiceTest {

    @Test
    void aBuyAdvertisementCanBePostedByARegisteredUser() {
        var buyer = registerUser();

        var buyAdvertisement = tradingService.postBuyAdvertisement(buyer.id(), CRYPTO_ACTIVE_SYMBOL, VALID_ADVERTISEMENT_QUANTITY, VALID_ADVERTISEMENT_PRICE);

        assertAdvertisementHas(CRYPTO_ACTIVE_SYMBOL, VALID_ADVERTISEMENT_QUANTITY, VALID_ADVERTISEMENT_PRICE, buyer, buyAdvertisement);
    }

    @Test
    void aSellAdvertisementCanBePostedByARegisteredUser() {
        var seller = registerUser();

        var sellAdvertisement = tradingService.postSellAdvertisement(seller.id(), CRYPTO_ACTIVE_SYMBOL, VALID_ADVERTISEMENT_QUANTITY, VALID_ADVERTISEMENT_PRICE);

        assertAdvertisementHas(CRYPTO_ACTIVE_SYMBOL, VALID_ADVERTISEMENT_QUANTITY, VALID_ADVERTISEMENT_PRICE, seller, sellAdvertisement);
    }

    @Test
    void postedSellAdvertisementsCanBeSearchByCryptoActiveSymbol() {
        var seller = registerUser();

        tradingService.postSellAdvertisement(seller.id(), CRYPTO_ACTIVE_SYMBOL, VALID_ADVERTISEMENT_QUANTITY, VALID_ADVERTISEMENT_PRICE);

        var foundSellAdvertisements = tradingService.findSellAdvertisementsWithSymbol(CRYPTO_ACTIVE_SYMBOL);

        assertEquals(1, foundSellAdvertisements.size());
        assertAdvertisementHas(CRYPTO_ACTIVE_SYMBOL, VALID_ADVERTISEMENT_QUANTITY, VALID_ADVERTISEMENT_PRICE, seller, foundSellAdvertisements.get(0));
    }

    @Test
    void postedSellAdvertisementsSearchResultsDoesNotIncludeBuyAdvertisementPosts() {
        var publisher = registerUser();
        var buyPrice = TWENTY_PESOS;
        var sellPrice = FOURTY_PESOS;
        tradingService.postBuyAdvertisement(publisher.id(), CRYPTO_ACTIVE_SYMBOL, VALID_ADVERTISEMENT_QUANTITY, buyPrice);
        tradingService.postSellAdvertisement(publisher.id(), CRYPTO_ACTIVE_SYMBOL, VALID_ADVERTISEMENT_QUANTITY, sellPrice);

        var foundSellAdvertisements = tradingService.findSellAdvertisementsWithSymbol(CRYPTO_ACTIVE_SYMBOL);

        assertEquals(1, foundSellAdvertisements.size());
        assertEquals(sellPrice, foundSellAdvertisements.get(0).price());
    }

    @Test
    void postedBuyAdvertisementsSearchResultsDoesNotIncludeSellAdvertisementPosts() {
        var publisher = registerUser();
        var buyPrice = TWENTY_PESOS;
        var sellPrice = FOURTY_PESOS;
        tradingService.postBuyAdvertisement(publisher.id(), CRYPTO_ACTIVE_SYMBOL, VALID_ADVERTISEMENT_QUANTITY, buyPrice);
        tradingService.postSellAdvertisement(publisher.id(), CRYPTO_ACTIVE_SYMBOL, VALID_ADVERTISEMENT_QUANTITY, sellPrice);

        var foundSellAdvertisements = tradingService.findBuyAdvertisementsWithSymbol(CRYPTO_ACTIVE_SYMBOL);

        assertEquals(1, foundSellAdvertisements.size());
        assertEquals(buyPrice, foundSellAdvertisements.get(0).price());
    }

    @Test
    void aSellAdvertisementCannotBePostedForANotRegisteredUser() {
        var notRegisteredUserId = 123L;

        assertThrowsDomainException(
                "User not found",
                () -> tradingService.postSellAdvertisement(notRegisteredUserId, CRYPTO_ACTIVE_SYMBOL, VALID_ADVERTISEMENT_QUANTITY, VALID_ADVERTISEMENT_PRICE));

        assertHasNoAdvertisementsFor(CRYPTO_ACTIVE_SYMBOL, assetAdvertisementsRepository);
    }

    @Test
    void aSellAdvertisementCannotBePostedWithAQuantityLesserThanOne() {
        var seller = registerUser();
        var invalidQuantity = 0;

        assertThrowsDomainException(
                "Quantity cannot be lesser than 1",
                () -> tradingService.postSellAdvertisement(seller.id(), CRYPTO_ACTIVE_SYMBOL, invalidQuantity, VALID_ADVERTISEMENT_PRICE));

        assertHasNoAdvertisementsFor(CRYPTO_ACTIVE_SYMBOL, assetAdvertisementsRepository);
    }

    @Test
    void aSellAdvertisementCannotBePostedWithANonPositiveAmountOfMoney() {
        var seller = registerUser();
        var invalidPrice = ZERO_PESOS;

        assertThrowsDomainException(
                "Price amount of money must be positive",
                () -> tradingService.postSellAdvertisement(seller.id(), CRYPTO_ACTIVE_SYMBOL, VALID_ADVERTISEMENT_QUANTITY, invalidPrice));

        assertHasNoAdvertisementsFor(CRYPTO_ACTIVE_SYMBOL, assetAdvertisementsRepository);
    }

}
