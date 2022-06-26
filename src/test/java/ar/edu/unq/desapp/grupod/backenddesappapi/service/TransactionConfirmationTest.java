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
    void aPublisherIncrementsHisNumberOfOperationsForEveryConfirmedTransaction() {
        var anInterestedUser = registerPepe();
        var aPublisher = registerJuan();
        var anAdvertisement = publishSellAdvertisementFor(aPublisher);

        tradingService.informTransaction(anInterestedUser.id(), anAdvertisement.id(), 1);
        var aTransactionToConfirm = tradingService.informTransaction(anInterestedUser.id(), anAdvertisement.id(), 2);

        tradingService.confirmTransaction(aPublisher.id(), aTransactionToConfirm.id());

        var publisherAfterTwoConfirmedTransactions = userService.findUserById(aPublisher.id());
        assertEquals(1, publisherAfterTwoConfirmedTransactions.numberOfOperations());
    }

    @Test
    void thePaymentAddressOfAConfirmedTransactionsForASellAdvertisementIsTheCVUOfThePublisher() {
        var anInterestedUser = registerPepe();
        var aPublisher = registerJuan();

        var aSellAdvertisement = publishSellAdvertisementFor(aPublisher);
        var aTransaction = tradingService.informTransaction(anInterestedUser.id(), aSellAdvertisement.id(), aSellAdvertisement.quantity());

        tradingService.confirmTransaction(aPublisher.id(), aTransaction.id());

        var confirmedTransaction = tradingService.findTransactionsInformedBy(anInterestedUser.id()).get(0);
        assertEquals(aPublisher.cvu(), confirmedTransaction.paymentAddress().get());
    }

    @Test
    void thePaymentAddressOfAConfirmedTransactionsForABuyAdvertisementIsTheCryptoActiveWalletAddressOfThePublisher() {
        var anInterestedUser = registerPepe();
        var aPublisher = registerJuan();

        var aBuyAdvertisement = publishBuyAdvertisementFor(aPublisher);
        var aTransaction = tradingService.informTransaction(anInterestedUser.id(), aBuyAdvertisement.id(), aBuyAdvertisement.quantity());

        tradingService.confirmTransaction(aPublisher.id(), aTransaction.id());

        var confirmedTransaction = tradingService.findTransactionsInformedBy(anInterestedUser.id()).get(0);
        assertEquals(aPublisher.cryptoActiveWalletAddress(), confirmedTransaction.paymentAddress().get());
    }

    @Test
    void aUserCannotConfirmATransactionForAnAdvertisementNotPublishedByHimself() {
        var anInterestedUser = registerPepe();
        var aPublisher = registerJuan();

        var anAdvertisement = publishAdvertisementFor(aPublisher);
        var aTransaction = tradingService.informTransaction(anInterestedUser.id(), anAdvertisement.id(), anAdvertisement.quantity());

        assertThrowsDomainException(
                "transaction.user_cannot_confirm_a_transaction_for_an_advertisement_published_by_himself",
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
                "transaction.not_found",
                () -> tradingService.confirmTransaction(aUser.id(), nonExistentTransactionId)
        );
    }

}
