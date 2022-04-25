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

        var aSellAdverticement = publishSellAdvertisementFor(aSeller);
        var aTransactionToConfirm = tradingService.informTransaction(aBuyer.id(), aSellAdverticement.id(), aSellAdverticement.quantity());

        tradingService.confirmTransaction(aSeller.id(), aTransactionToConfirm.id());

        var foundSellAdvertisements = tradingService.findSellAdvertisementsWithSymbol(aSellAdverticement.assetSymbol());
        assertTrue(foundSellAdvertisements.isEmpty());
    }

    @Test
    void aUserCannotConfirmATransactionPlacedByHimself() {
        var aBuyer = registerPepe();
        var aSeller = registerJuan();

        var aSellAdvertisement = publishSellAdvertisementFor(aSeller);
        var aBuyOrder = tradingService.informTransaction(aBuyer.id(), aSellAdvertisement.id(), aSellAdvertisement.quantity());

        assertThrowsDomainException(
                "A user cannot confirm an order placed by himself",
                () -> tradingService.confirmTransaction(aBuyer.id(), aBuyOrder.id())
        );
    }

}
