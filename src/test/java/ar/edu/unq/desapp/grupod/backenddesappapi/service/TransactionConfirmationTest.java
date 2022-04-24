package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TransactionConfirmationTest extends ServiceTest {

    @Test
    void aSellerCanConfirmABuyOrderForOneOfHisSellAdvertisements() {
        var aBuyer = registerPepe();
        var aSeller = registerJuan();

        var aSellAdverticement = publishSellAdverticementFor(aSeller);
        var buyOrderToConfirm = tradeService.informTransaction(aBuyer.id(), aSellAdverticement.id(), aSellAdverticement.quantity());

        tradeService.confirmTransaction(aSeller.id(), buyOrderToConfirm.id());

        var orders = tradeService.findTransactionsInformedBy(aBuyer.id());

        assertFalse(orders.get(0).isPending());
        assertTrue(orders.get(0).isConfirmed());
    }

    @Test
    void whenASellerConfirmsABuyOrderToBuySomeQuantityFromAnAdvertisementItsAvailableQuantityIsDecreased() {
        var originalQuantityToSell = 3;
        var quantityToBuy = 1;

        var aBuyer = registerPepe();
        var aSeller = registerJuan();

        var aSellAdverticement = publishSellAdverticementFor(aSeller, originalQuantityToSell);
        var aBuyOrder = tradeService.informTransaction(aBuyer.id(), aSellAdverticement.id(), quantityToBuy);

        tradeService.confirmTransaction(aSeller.id(), aBuyOrder.id());

        var foundSellAdvertisements = tradeService.findSellAdvertisementsWithSymbol(aSellAdverticement.assetSymbol());
        assertEquals(1, foundSellAdvertisements.size());
        assertEquals(originalQuantityToSell - quantityToBuy, foundSellAdvertisements.get(0).quantity());
    }

    @Test
    void whenASellerConfirmsASellOrderToBuyAllAvailableQuantityFromAnAdvertisementItIsRemovedFromTheListOfPublishedAdvertisements() {
        var aBuyer = registerPepe();
        var aSeller = registerJuan();

        var aSellAdverticement = publishSellAdverticementFor(aSeller);
        var aBuyOrder = tradeService.informTransaction(aBuyer.id(), aSellAdverticement.id(), aSellAdverticement.quantity());

        tradeService.confirmTransaction(aSeller.id(), aBuyOrder.id());

        var foundSellAdvertisements = tradeService.findSellAdvertisementsWithSymbol(aSellAdverticement.assetSymbol());
        assertTrue(foundSellAdvertisements.isEmpty());
    }

    @Test
    void aUserCannotConfirmABuyOrderPlacedByHimself() {
        var aBuyer = registerPepe();
        var aSeller = registerJuan();

        var aSellAdverticement = publishSellAdverticementFor(aSeller);
        var aBuyOrder = tradeService.informTransaction(aBuyer.id(), aSellAdverticement.id(), aSellAdverticement.quantity());

        assertThrowsDomainExeption(
                "A user cannot confirm an order placed by himself",
                () -> tradeService.confirmTransaction(aBuyer.id(), aBuyOrder.id())
        );
    }

}
