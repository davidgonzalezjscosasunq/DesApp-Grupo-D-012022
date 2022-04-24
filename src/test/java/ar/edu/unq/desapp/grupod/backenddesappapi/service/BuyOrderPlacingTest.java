package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BuyOrderPlacingTest extends ServiceTest {

    @Test
    void aUserCanPlaceABuyOrderForASellAdvertisementPublishedByAnotherUser() {
        var aBuyer = registerPepe();
        var aSeller = registerJuan();
        var aSellAdverticement = publishSellAdverticementFor(aSeller);

        var symbolToBuy = aSellAdverticement.cryptoActiveSymbol();
        var quantityToBuy = aSellAdverticement.quantity();

        var aSellOrder = tradeService.placeBuyOrder(aBuyer.id(), aSellAdverticement.id(), quantityToBuy);

        assertTrue(aSellOrder.wasPlaceBy(aBuyer));
        assertEquals(symbolToBuy, aSellOrder.symbol());
        assertEquals(quantityToBuy, aSellOrder.quantity());
        assertTrue(aSellOrder.isPending());
    }

    @Test
    void aUserCanPlaceManyBuyOrdersForASellAdvertisementPublishedByAnotherUser() {
        var aBuyer = registerPepe();
        var aSeller = registerJuan();

        var quantityOfTheFirstBuyOrder = 6;
        var quantityOfTheSecondBuyOrder = 4;
        var sellAdvertisementQuantity = quantityOfTheFirstBuyOrder + quantityOfTheSecondBuyOrder;

        var aSellAdverticement = publishSellAdverticementFor(aSeller, sellAdvertisementQuantity);
        var symbolToBuy = aSellAdverticement.cryptoActiveSymbol();

        tradeService.placeBuyOrder(aBuyer.id(), aSellAdverticement.id(), quantityOfTheFirstBuyOrder);
        tradeService.placeBuyOrder(aBuyer.id(), aSellAdverticement.id(), quantityOfTheSecondBuyOrder);

        var orders = tradeService.ordersOf(aBuyer.id());

        assertEquals(2, orders.size());

        assertTrue(orders.get(0).wasPlaceBy(aBuyer));
        assertEquals(symbolToBuy, orders.get(0).symbol());
        assertTrue(orders.get(0).isPending());
        assertFalse(orders.get(0).isConfirmed());

        assertTrue(orders.get(1).wasPlaceBy(aBuyer));
        assertEquals(symbolToBuy, orders.get(1).symbol());
        assertTrue(orders.get(1).isPending());
        assertFalse(orders.get(0).isConfirmed());
    }

    @Test
    void aUserCannotPlaceABuyOrderForASellAdvertisementPublishedByHimself() {
        var aUser = registerPepe();

        var aSellAdverticement = publishSellAdverticementFor(aUser);

        assertThrowsDomainExeption(
                "Buyer and seller can't be the same user",
                () -> tradeService.placeBuyOrder(aUser.id(), aSellAdverticement.id(), aSellAdverticement.quantity())
        );

        assertHasNoOrders(aUser);
    }

    @Test
    void aUserCannotPlaceAnOrderWithAQuantityLesserThanOne() {
        var aBuyer = registerPepe();
        var aSeller = registerJuan();

        var aSellAdverticement = publishSellAdverticementFor(aSeller);

        var invalidQuantityLesserThanOne = 0;

        assertThrowsDomainExeption(
                "Quantity to buy must be greater than zero",
                () -> tradeService.placeBuyOrder(aBuyer.id(), aSellAdverticement.id(), invalidQuantityLesserThanOne)
        );

        assertHasNoOrders(aBuyer);
    }

    @Test
    void aBuyOrderCannotBePlacedWithANotRegisteredBuyerId() {
        var aSeller = registerJuan();
        var aSellAdverticement = publishSellAdverticementFor(aSeller);

        var notRegisteredBuyerId = 123L;

        assertThrowsDomainExeption(
                "User not found",
                () -> tradeService.placeBuyOrder(notRegisteredBuyerId, aSellAdverticement.id(), aSellAdverticement.quantity())
        );
    }

    @Test
    void aBuyOrderCannotBePlacedWithANotRegisteredSellAdvertisementId() {
        var aBuyer = registerPepe();
        var quantityToBuy = 1;

        var notRegisterdSellAdvertisementId = 123L;

        assertThrowsDomainExeption(
                "Adverticement not found",
                () -> tradeService.placeBuyOrder(aBuyer.id(), notRegisterdSellAdvertisementId, quantityToBuy)
        );

        assertHasNoOrders(aBuyer);
    }

    @Test
    void aUserCannotPlaceABuyOrderForABuyAdvertisement() {
        var aBuyer = registerPepe();
        var aSeller = registerJuan();
        var aBuyAdverticement = publishBuyAdverticementFor(aSeller);

        assertThrowsDomainExeption(
                "Cannot place a buy order for a buy advertisement",
                () -> tradeService.placeBuyOrder(aBuyer.id(), aBuyAdverticement.id(), aBuyAdverticement.quantity())
        );

        assertHasNoOrders(aBuyer);
    }

}
