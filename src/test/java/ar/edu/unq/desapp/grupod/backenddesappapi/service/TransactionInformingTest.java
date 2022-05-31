package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransactionInformingTest extends ServiceTest {

    @Test
    void aUserCanInformATransactionForAnAdvertisementPublishedByAnotherUser() {
        var anInterestedUser = registerPepe();
        var aPublisher = registerJuan();
        var anAdvertisement = publishAdvertisementFor(aPublisher);

        var assetSymbol = anAdvertisement.assetSymbol();
        var quantity = anAdvertisement.quantity();

        var transaction = tradingService.informTransaction(anInterestedUser.id(), anAdvertisement.id(), quantity);

        assertIsPendingTransactionWith(anInterestedUser, assetSymbol, quantity, transaction);
    }

    @Test
    void aUserCanInformManyTransactionsForAnAdvertisementPublishedByAnotherUser() {
        var anInterestedUser = registerPepe();
        var aPublisher = registerJuan();

        var quantityOfTheFirstTransaction = 6;
        var quantityOfTheSecondTransaction = 4;
        var advertisementQuantity = quantityOfTheFirstTransaction + quantityOfTheSecondTransaction;

        var anAdvertisement = publishAdvertisementFor(aPublisher, advertisementQuantity);
        var advertisementAssetSymbol = anAdvertisement.assetSymbol();

        tradingService.informTransaction(anInterestedUser.id(), anAdvertisement.id(), quantityOfTheFirstTransaction);
        tradingService.informTransaction(anInterestedUser.id(), anAdvertisement.id(), quantityOfTheSecondTransaction);

        var transactions = tradingService.findTransactionsInformedBy(anInterestedUser.id());

        assertEquals(2, transactions.size());
        assertIsPendingTransactionWith(anInterestedUser, advertisementAssetSymbol, quantityOfTheFirstTransaction, transactions.get(0));
        assertIsPendingTransactionWith(anInterestedUser, advertisementAssetSymbol, quantityOfTheSecondTransaction, transactions.get(1));
    }

    @Test
    void aPendingTransactionsHasNoPaymentAddress() {
        var anInterestedUser = registerPepe();
        var aPublisher = registerJuan();

        var anAdvertisement = publishAdvertisementFor(aPublisher);
        tradingService.informTransaction(anInterestedUser.id(), anAdvertisement.id(), anAdvertisement.quantity());

        var pendingTransaction = tradingService.findTransactionsInformedBy(anInterestedUser.id()).get(0);
        assertTrue(pendingTransaction.paymentAddress().isEmpty());
    }

    @Test
    void aUserCannotInformATransactionForAnAdvertisementPublishedByHimself() {
        var aUser = registerPepe();

        var anAdvertisement = publishAdvertisementFor(aUser);

        assertThrowsDomainException(
                "transaction.user_cannot_inform_a_transaction_for_an_advertisement_published_by_himself",
                () -> tradingService.informTransaction(aUser.id(), anAdvertisement.id(), anAdvertisement.quantity())
        );

        assertHasNoInformedTransactions(aUser);
    }

    @Test
    void aUserCannotInformATransactionWithAQuantityLesserThanOne() {
        var anInterestedUser = registerPepe();
        var aPublisher = registerJuan();

        var anAdvertisement = publishAdvertisementFor(aPublisher);

        var invalidQuantityLesserThanOne = 0;

        assertThrowsDomainException(
                "transaction.quantity_must_be_greater_than_zero",
                () -> tradingService.informTransaction(anInterestedUser.id(), anAdvertisement.id(), invalidQuantityLesserThanOne)
        );

        assertHasNoInformedTransactions(anInterestedUser);
    }

    @Test
    void aTransactionCannotBeInformedWithANotRegisteredUserId() {
        var aPublisher = registerJuan();
        var anAdvertisement = publishAdvertisementFor(aPublisher);

        var notRegisteredUserId = 123L;

        assertThrowsDomainException(
                "user.not_found",
                () -> tradingService.informTransaction(notRegisteredUserId, anAdvertisement.id(), anAdvertisement.quantity())
        );
    }

    @Test
    void aTransactionCannotBeInformedWithANotRegisteredAdvertisementId() {
        var anInterestedUser = registerPepe();
        var quantity = 1;

        var notRegisteredAdvertisementId = 123L;

        assertThrowsDomainException(
                "advertisement.not_found",
                () -> tradingService.informTransaction(anInterestedUser.id(), notRegisteredAdvertisementId, quantity)
        );

        assertHasNoInformedTransactions(anInterestedUser);
    }

}
