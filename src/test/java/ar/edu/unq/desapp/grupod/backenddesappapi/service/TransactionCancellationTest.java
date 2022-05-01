package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransactionCancellationTest extends ServiceTest {

    @Test
    void aTransactionForAnAdvertisementsCanBeCancelledByThePublisherOfThatAdvertisement() {
        var anInterestedUser = registerPepe();
        var aPublisher = registerJuan();

        var anAdvertisement = publishAdvertisementFor(aPublisher);
        var transactionToConfirm = informTransactionForAllQuantity(anInterestedUser, anAdvertisement);

        tradingService.cancelTransaction(aPublisher.id(), transactionToConfirm.id());

        var cancelledTransaction = tradingService.findTransactionsInformedBy(anInterestedUser.id()).get(0);
        assertTrue(cancelledTransaction.isCancelled());
    }

    @Test
    void aTransactionForAnAdvertisementsCanBeCancelledByUserThatInformedIt() {
        var anInterestedUser = registerPepe();
        var aPublisher = registerJuan();

        var anAdvertisement = publishAdvertisementFor(aPublisher);
        var transactionToConfirm = informTransactionForAllQuantity(anInterestedUser, anAdvertisement);

        tradingService.cancelTransaction(anInterestedUser.id(), transactionToConfirm.id());

        var cancelledTransaction = tradingService.findTransactionsInformedBy(anInterestedUser.id()).get(0);
        assertTrue(cancelledTransaction.isCancelled());
    }

    @Test
    void whenATransactionForAnAdvertisementsIsCancelledByPublisherOfThatAdvertisementHeLooses20ReputationPoints() {
        var anInterestedUser = registerPepe();
        var aPublisher = registerJuan();

        var anAdvertisement = publishAdvertisementFor(aPublisher);
        var transactionToConfirm = informTransactionForAllQuantity(anInterestedUser, anAdvertisement);

        tradingService.cancelTransaction(aPublisher.id(), transactionToConfirm.id());
        assertEquals(-20, userService.reputationPointsOf(aPublisher.id()));
    }

    @Test
    void whenATransactionForAnAdvertisementsIsCancelledByTheUserThatInformedItThePublisherLooses20ReputationPoints() {
        var anInterestedUser = registerPepe();
        var aPublisher = registerJuan();

        var anAdvertisement = publishAdvertisementFor(aPublisher);
        var transactionToConfirm = informTransactionForAllQuantity(anInterestedUser, anAdvertisement);

        tradingService.cancelTransaction(anInterestedUser.id(), transactionToConfirm.id());
        assertEquals(-20, userService.reputationPointsOf(aPublisher.id()));
    }

    @Test
    void aTransactionForAnAdvertisementsCannotBeCancelledByAUserDifferentToThePublisherOfTheAdvertisementOrTheUserThatInformIt() {
        var anInterestedUser = registerPepe();
        var aPublisher = registerJuan();
        var anotherUser = registerPedro();

        var anAdvertisement = publishAdvertisementFor(aPublisher);
        var transactionToConfirm = informTransactionForAllQuantity(anInterestedUser, anAdvertisement);

        assertThrowsDomainException(
                "A transaction can only be cancelled by the user that informed it or the user that published the advertisement",
                () -> tradingService.cancelTransaction(anotherUser.id(), transactionToConfirm.id())
        );

        var cancelledTransaction = tradingService.findTransactionsInformedBy(anInterestedUser.id()).get(0);
        assertFalse(cancelledTransaction.isCancelled());
        assertEquals(0, userService.reputationPointsOf(aPublisher.id()));
    }

}
