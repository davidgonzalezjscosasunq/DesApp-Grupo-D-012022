package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.User;
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

        assertHasReputationPointsEqualTo(-20, aPublisher);
    }

    @Test
    void whenATransactionForAnAdvertisementsIsCancelledByTheUserThatInformedItThePublisherLooses20ReputationPoints() {
        var anInterestedUser = registerPepe();
        var aPublisher = registerJuan();

        var anAdvertisement = publishAdvertisementFor(aPublisher);
        var transactionToConfirm = informTransactionForAllQuantity(anInterestedUser, anAdvertisement);

        tradingService.cancelTransaction(anInterestedUser.id(), transactionToConfirm.id());

        assertHasReputationPointsEqualTo(-20, aPublisher);
    }

    @Test
    void aTransactionForAnAdvertisementsCannotBeCancelledByAUserDifferentToThePublisherOfTheAdvertisementOrTheUserThatInformIt() {
        var anInterestedUser = registerPepe();
        var aPublisher = registerJuan();
        var anotherUser = registerPedro();

        var anAdvertisement = publishAdvertisementFor(aPublisher);
        var transactionToConfirm = informTransactionForAllQuantity(anInterestedUser, anAdvertisement);

        assertThrowsDomainException(
                "transaction.can_only_be_cancelled_by_the_user_that_informed_it_or_the_user_that_published_the_advertisement",
                () -> tradingService.cancelTransaction(anotherUser.id(), transactionToConfirm.id())
        );

        var cancelledTransaction = tradingService.findTransactionsInformedBy(anInterestedUser.id()).get(0);
        assertFalse(cancelledTransaction.isCancelled());
        assertHasReputationPointsEqualTo(0, aPublisher);
    }

    @Test
    void aCancelledTransactionsHasNoPaymentAddress() {
        var anInterestedUser = registerPepe();
        var aPublisher = registerJuan();

        var anAdvertisement = publishAdvertisementFor(aPublisher);
        var transaction = tradingService.informTransaction(anInterestedUser.id(), anAdvertisement.id(), anAdvertisement.quantity());

        tradingService.cancelTransaction(aPublisher.id(), transaction.id());

        var pendingTransaction = tradingService.findTransactionsInformedBy(anInterestedUser.id()).get(0);
        assertTrue(pendingTransaction.paymentAddress().isEmpty());
    }

    @Test
    void aUserCannotCancelATransactionWithAnInvalidTransactionId() {
        var aUser = registerPepe();
        var nonExistentTransactionId = 123L;

        assertThrowsDomainException(
                "transaction.not_found",
                () -> tradingService.cancelTransaction(aUser.id(), nonExistentTransactionId)
        );
    }

    @Test
    void aTransactionForAnAdvertisementsCannotBeCancelledByANotRegisteredUser() {
        var anInterestedUser = registerPepe();
        var aPublisher = registerJuan();

        var anAdvertisement = publishAdvertisementFor(aPublisher);
        var transactionToConfirm = informTransactionForAllQuantity(anInterestedUser, anAdvertisement);

        var notRegisteredUserId = -999999L;

        assertThrowsDomainException(
                "user.not_found",
                () -> tradingService.cancelTransaction(notRegisteredUserId, transactionToConfirm.id())
        );

        var cancelledTransaction = tradingService.findTransactionsInformedBy(anInterestedUser.id()).get(0);
        assertFalse(cancelledTransaction.isCancelled());
        assertHasReputationPointsEqualTo(0, aPublisher);
    }

}
