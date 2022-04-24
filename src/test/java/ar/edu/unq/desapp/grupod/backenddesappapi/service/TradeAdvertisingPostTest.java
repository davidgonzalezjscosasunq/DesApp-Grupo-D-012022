package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TradeAdvertisingPostTest extends ServiceTest {

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

        assertHasNoAdvertisementsFor(CRYPTO_ACTIVE_SYMBOL, cryptoAdvertisementsRepository);
    }

    @Test
    void aSellAdvertisementCannotBePostedWithAQuantityLesserThanOne() {
        var seller = registerUser();
        var invalidQuantity = 0;

        assertThrowsDomainExeption(
                "Quantity cannot be lesser than 1",
                () -> tradeService.postSellAdvertisement(seller.id(), CRYPTO_ACTIVE_SYMBOL, invalidQuantity, VALID_ADVERTISEMENT_PRICE));

        assertHasNoAdvertisementsFor(CRYPTO_ACTIVE_SYMBOL, cryptoAdvertisementsRepository);
    }

    @Test
    void aSellAdvertisementCannotBePostedWithANonPositiveAmountOfMoney() {
        var seller = registerUser();
        var invalidPrice = ZERO_PESOS;

        assertThrowsDomainExeption(
                "Price amount of money must be positive",
                () -> tradeService.postSellAdvertisement(seller.id(), CRYPTO_ACTIVE_SYMBOL, VALID_ADVERTISEMENT_QUANTITY, invalidPrice));

        assertHasNoAdvertisementsFor(CRYPTO_ACTIVE_SYMBOL, cryptoAdvertisementsRepository);
    }

}
