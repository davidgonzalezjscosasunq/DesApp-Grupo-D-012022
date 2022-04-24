package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BuyOrderConfirmationTest extends ServiceTest {

    @Test
    void aSellerCanConfirmABuyOrderForOneOfHisSellAdvertisements() {
        var aBuyer = registerPepe();
        var aSeller = registerJuan();

        var aSellAdverticement = publishSellAdverticementFor(aSeller);
        var buyOrderToConfirm = tradeService.placeBuyOrder(aBuyer.id(), aSellAdverticement.id(), aSellAdverticement.quantity());

        tradeService.confirmSuccessfulSell(aSeller.id(), buyOrderToConfirm.id());

        var orders = tradeService.ordersOf(aBuyer.id());

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
        var aBuyOrder = tradeService.placeBuyOrder(aBuyer.id(), aSellAdverticement.id(), quantityToBuy);

        tradeService.confirmSuccessfulSell(aSeller.id(), aBuyOrder.id());

        var foundSellAdvertisements = tradeService.findSellAdvertisementsWithSymbol(aSellAdverticement.cryptoActiveSymbol());
        assertEquals(1, foundSellAdvertisements.size());
        assertEquals(originalQuantityToSell - quantityToBuy, foundSellAdvertisements.get(0).quantity());
    }

    @Test
    void whenASellerConfirmsASellOrderToBuyAllAvailableQuantityFromAnAdvertisementItIsRemovedFromTheListOfPublishedAdvertisements() {
        var aBuyer = registerPepe();
        var aSeller = registerJuan();

        var aSellAdverticement = publishSellAdverticementFor(aSeller);
        var aBuyOrder = tradeService.placeBuyOrder(aBuyer.id(), aSellAdverticement.id(), aSellAdverticement.quantity());

        tradeService.confirmSuccessfulSell(aSeller.id(), aBuyOrder.id());

        var foundSellAdvertisements = tradeService.findSellAdvertisementsWithSymbol(aSellAdverticement.cryptoActiveSymbol());
        assertTrue(foundSellAdvertisements.isEmpty());
    }

    @Test
    void aUserCannotConfirmABuyOrderPlacedByHimself() {
        var aBuyer = registerPepe();
        var aSeller = registerJuan();

        var aSellAdverticement = publishSellAdverticementFor(aSeller);
        var aBuyOrder = tradeService.placeBuyOrder(aBuyer.id(), aSellAdverticement.id(), aSellAdverticement.quantity());

        assertThrowsDomainExeption(
                "A user cannot confirm an order placed by himself",
                () -> tradeService.confirmSuccessfulSell(aBuyer.id(), aBuyOrder.id())
        );
    }

}
