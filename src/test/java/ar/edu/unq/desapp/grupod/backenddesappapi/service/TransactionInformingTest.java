package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import ar.edu.unq.desapp.grupod.backenddesappapi.model.CryptoAdvertisement;
import ar.edu.unq.desapp.grupod.backenddesappapi.model.User;
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
    void aUserCannotInformATransactionForAnAdvertisementPublishedByHimself() {
        var aUser = registerPepe();

        var anAdvertisement = publishAdvertisementFor(aUser);

        assertThrowsDomainExeption(
                "A user cannot inform a transaction for an advertisement published by himself",
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

        assertThrowsDomainExeption(
                "A transaction quantity must be greater than zero",
                () -> tradingService.informTransaction(anInterestedUser.id(), anAdvertisement.id(), invalidQuantityLesserThanOne)
        );

        assertHasNoInformedTransactions(anInterestedUser);
    }

    @Test
    void aTransactionCannotBeInformedWithANotRegisteredUserId() {
        var aPublisher = registerJuan();
        var anAdvertisement = publishAdvertisementFor(aPublisher);

        var notRegisteredUserId = 123L;

        assertThrowsDomainExeption(
                "User not found",
                () -> tradingService.informTransaction(notRegisteredUserId, anAdvertisement.id(), anAdvertisement.quantity())
        );
    }

    @Test
    void aTransactionCannotBeInformedWithANotRegisteredAdvertisementId() {
        var anInterestedUser = registerPepe();
        var quantity = 1;

        var notRegisteredAdvertisementId = 123L;

        assertThrowsDomainExeption(
                "Advertisement not found",
                () -> tradingService.informTransaction(anInterestedUser.id(), notRegisteredAdvertisementId, quantity)
        );

        assertHasNoInformedTransactions(anInterestedUser);
    }

}
