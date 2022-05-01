package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransactionConfirmationTest extends ServiceTest {

    @Test
    void aPublisherCanConfirmATransactionForOneOfHisAdvertisements() {
        var anInterestedUser = registerPepe();
        var aPublisher = registerJuan();

        var anAdvertisement = publishAdvertisementFor(aPublisher);
        var transactionToConfirm = tradingService.informTransaction(anInterestedUser.id(), anAdvertisement.id(), anAdvertisement.quantity());

        tradingService.confirmTransaction(aPublisher.id(), transactionToConfirm.id());

        var transactions = tradingService.findTransactionsInformedBy(anInterestedUser.id());

        assertFalse(transactions.get(0).isPending());
        assertTrue(transactions.get(0).isConfirmed());
    }

    @Test
    void whenAPublisherConfirmsATransactionForSomeQuantityOfAnAdvertisementItsAvailableQuantityIsDecreased() {
        var originalQuantityOfAdvertisement = 3;
        var quantityOfTransaction = 1;

        var anInterestedUser = registerPepe();
        var aPublisher = registerJuan();

        var anAdvertisement = publishAdvertisementFor(aPublisher, originalQuantityOfAdvertisement);
        var aTransactionToConfirm = tradingService.informTransaction(anInterestedUser.id(), anAdvertisement.id(), quantityOfTransaction);

        tradingService.confirmTransaction(aPublisher.id(), aTransactionToConfirm.id());

        var foundAdvertisements = tradingService.findAdvertisementsWithSymbol(anAdvertisement.assetSymbol());
        assertEquals(1, foundAdvertisements.size());
        assertEquals(originalQuantityOfAdvertisement - quantityOfTransaction, foundAdvertisements.get(0).quantity());
    }

    @Test
    void whenAPublisherConfirmsATransactionForAllAvailableQuantityOfAnAdvertisementItIsRemovedFromTheListOfPublishedAdvertisements() {
        var anInterestedUser = registerPepe();
        var aPublisher = registerJuan();

        var anAdvertisement = publishAdvertisementFor(aPublisher);
        var aTransactionToConfirm = tradingService.informTransaction(anInterestedUser.id(), anAdvertisement.id(), anAdvertisement.quantity());

        tradingService.confirmTransaction(aPublisher.id(), aTransactionToConfirm.id());

        var foundAdvertisements = tradingService.findAdvertisementsWithSymbol(anAdvertisement.assetSymbol());
        assertTrue(foundAdvertisements.isEmpty());
    }

    @Test
    void aNewUserHasNoPoints() {
        var aUser = registerUser();

        assertEquals(0, userService.reputationPointsOf(aUser.id()));
    }

    @Test
    void whenAPublisherConfirmsATransactionInLessThan30MinutesSinceItWasInformedItGets10ReputationPoints() {
        var anInterestedUser = registerPepe();
        var aPublisher = registerJuan();

        var anAdvertisement = publishSellAdvertisementFor(aPublisher);
        var transactionToConfirm = tradingService.informTransaction(anInterestedUser.id(), anAdvertisement.id(), anAdvertisement.quantity());

        clock.advanceMinutes(29);

        tradingService.confirmTransaction(aPublisher.id(), transactionToConfirm.id());

        assertEquals(10, userService.reputationPointsOf(aPublisher.id()));
    }

    @Test
    void whenAPublisherConfirmsATransaction30AfterItWasInformedItGets5ReputationPoints() {
        var anInterestedUser = registerPepe();
        var aPublisher = registerJuan();

        var anAdvertisement = publishSellAdvertisementFor(aPublisher);
        var transactionToConfirm = tradingService.informTransaction(anInterestedUser.id(), anAdvertisement.id(), anAdvertisement.quantity());

        clock.advanceMinutes(30);

        tradingService.confirmTransaction(aPublisher.id(), transactionToConfirm.id());

        assertEquals(5, userService.reputationPointsOf(aPublisher.id()));
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

    @Test
    void aUserCannotConfirmATransactionWithAnInvalidTransactionId() {
        var aUser = registerPepe();
        var nonExistentTransactionId = 123L;

        assertThrowsDomainException(
                "Transaction not found",
                () -> tradingService.confirmTransaction(aUser.id(), nonExistentTransactionId)
        );
    }

}
