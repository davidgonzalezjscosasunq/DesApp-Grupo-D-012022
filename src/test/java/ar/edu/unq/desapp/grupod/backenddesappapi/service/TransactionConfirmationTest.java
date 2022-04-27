package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransactionConfirmationTest extends ServiceTest {

    @Test
    void aSellerCanConfirmATransactionForOneOfHisSellAdvertisements() {
        var aBuyer = registerPepe();
        var aSeller = registerJuan();

        var aSellAdvertisement = publishSellAdvertisementFor(aSeller);
        var transactionToConfirm = tradingService.informTransaction(aBuyer.id(), aSellAdvertisement.id(), aSellAdvertisement.quantity());

        tradingService.confirmTransaction(aSeller.id(), transactionToConfirm.id());

        var transactions = tradingService.findTransactionsInformedBy(aBuyer.id());

        assertFalse(transactions.get(0).isPending());
        assertTrue(transactions.get(0).isConfirmed());
    }

    @Test
    void whenASellerConfirmsATransactionToBuySomeQuantityFromAnAdvertisementItsAvailableQuantityIsDecreased() {
        var originalQuantityToSell = 3;
        var quantityToBuy = 1;

        var aBuyer = registerPepe();
        var aSeller = registerJuan();

        var aSellAdvertisement = publishSellAdvertisementFor(aSeller, originalQuantityToSell);
        var aTransactionToConfirm = tradingService.informTransaction(aBuyer.id(), aSellAdvertisement.id(), quantityToBuy);

        tradingService.confirmTransaction(aSeller.id(), aTransactionToConfirm.id());

        var foundSellAdvertisements = tradingService.findSellAdvertisementsWithSymbol(aSellAdvertisement.assetSymbol());
        assertEquals(1, foundSellAdvertisements.size());
        assertEquals(originalQuantityToSell - quantityToBuy, foundSellAdvertisements.get(0).quantity());
    }

    @Test
    void whenASellerConfirmsATransactionToBuyAllAvailableQuantityFromAnAdvertisementItIsRemovedFromTheListOfPublishedAdvertisements() {
        var aBuyer = registerPepe();
        var aSeller = registerJuan();

        var aSellAdvertisement = publishSellAdvertisementFor(aSeller);
        var aTransactionToConfirm = tradingService.informTransaction(aBuyer.id(), aSellAdvertisement.id(), aSellAdvertisement.quantity());

        tradingService.confirmTransaction(aSeller.id(), aTransactionToConfirm.id());

        var foundSellAdvertisements = tradingService.findSellAdvertisementsWithSymbol(aSellAdvertisement.assetSymbol());
        assertTrue(foundSellAdvertisements.isEmpty());
    }

    @Test
    void aNewUserHasNoPoints() {
        var aUser = registerUser();

        assertEquals(0, userService.repotatinoPointsOf(aUser.id()));
    }

    @Test
    void whenAPublisherConfirmsATransactionInLessThan30MinutesSinceItWasInformedItGets10ReputationPoints() {
        var anInterestedUser = registerPepe();
        var aPublisher = registerJuan();

        var anAdvertisement = publishSellAdvertisementFor(aPublisher);
        var transactionToConfirm = tradingService.informTransaction(anInterestedUser.id(), anAdvertisement.id(), anAdvertisement.quantity());

        clock.advanceMinutes(29);

        tradingService.confirmTransaction(aPublisher.id(), transactionToConfirm.id());

        assertEquals(10, userService.repotatinoPointsOf(aPublisher.id()));
    }

    @Test
    void whenAPublisherConfirmsATransaction30AfterItWasInformedItGets5ReputationPoints() {
        var anInterestedUser = registerPepe();
        var aPublisher = registerJuan();

        var anAdvertisement = publishSellAdvertisementFor(aPublisher);
        var transactionToConfirm = tradingService.informTransaction(anInterestedUser.id(), anAdvertisement.id(), anAdvertisement.quantity());

        clock.advanceMinutes(30);

        tradingService.confirmTransaction(aPublisher.id(), transactionToConfirm.id());

        assertEquals(5, userService.repotatinoPointsOf(aPublisher.id()));
    }

    @Test
    void aUserCannotConfirmATransactionForAnAdvertisementNotPublishedByHimself() {
        var anInterestedUser = registerPepe();
        var aPublisher = registerJuan();

        var anAdvertisement = publishAdvertisementFor(aPublisher);
        var aTransaction = tradingService.informTransaction(anInterestedUser.id(), anAdvertisement.id(), anAdvertisement.quantity());

        assertThrowsDomainException(
                "A user cannot confirm a transaction for an advertisement not published by himself",
                () -> tradingService.confirmTransaction(anInterestedUser.id(), aTransaction.id())
        );

        var transactionNotConfirmed = tradingService.findTransactionsInformedBy(anInterestedUser.id()).get(0);
        assertTrue(transactionNotConfirmed.isPending());
        assertFalse(transactionNotConfirmed.isConfirmed());
    }

}
