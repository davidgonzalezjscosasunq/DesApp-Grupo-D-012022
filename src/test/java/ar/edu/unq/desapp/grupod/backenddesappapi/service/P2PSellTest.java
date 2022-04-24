package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class P2PSellTest extends ServiceTest {

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
    }

    @Test
    void aUserCanPlaceManyBuyOrdersForASellAdvertisementPublishedByAnotherUser() {
        var aBuyer = registerPepe();
        var aSeller = registerJuan();

        var quantityToBuyForTheFirstOrder = 6;
        var quantityToBuyForTheSecondOrder = 4;
        var sellAdvertisementQuantity = quantityToBuyForTheFirstOrder + quantityToBuyForTheSecondOrder;

        var aSellAdverticement = publishSellAdverticementFor(aSeller, sellAdvertisementQuantity);
        var symbolToBuy = aSellAdverticement.cryptoActiveSymbol();

        tradeService.placeBuyOrder(aBuyer.id(), aSellAdverticement.id(), quantityToBuyForTheFirstOrder);

        tradeService.placeBuyOrder(aBuyer.id(), aSellAdverticement.id(), quantityToBuyForTheSecondOrder);

        var pendingOrders = tradeService.pendingOrdersOf(aBuyer.id());

        assertEquals(2, pendingOrders.size());

        assertTrue(pendingOrders.get(0).wasPlaceBy(aBuyer));
        assertEquals(symbolToBuy, pendingOrders.get(0).symbol());

        assertTrue(pendingOrders.get(1).wasPlaceBy(aBuyer));
        assertEquals(symbolToBuy, pendingOrders.get(1).symbol());
    }

    @Test
    void whenASellerConfirmsASellOrderTheBuyOrderIsNotPendingAnyMore() {
        var aBuyer = registerPepe();
        var aSeller = registerJuan();

        var quantityToBuyForTheOrderToConfirm = 6;
        var quantityToBuyForTheOrderNotToBeConfirmed = 4;
        var aSellAdverticement = publishSellAdverticementFor(aSeller, quantityToBuyForTheOrderToConfirm + quantityToBuyForTheOrderNotToBeConfirmed);

        var buyOrderNotToBeConfirmed = tradeService.placeBuyOrder(aBuyer.id(), aSellAdverticement.id(), quantityToBuyForTheOrderNotToBeConfirmed);
        var buyOrderToConfirm = tradeService.placeBuyOrder(aBuyer.id(), aSellAdverticement.id(), quantityToBuyForTheOrderToConfirm);

        tradeService.confirmSuccessfulSell(aSeller.id(), buyOrderToConfirm.id());

        var pendingOrders = tradeService.pendingOrdersOf(aBuyer.id());

        assertEquals(1, pendingOrders.size());

        assertTrue(pendingOrders.get(0).wasPlaceBy(aBuyer));
        assertEquals(buyOrderNotToBeConfirmed.symbol(), pendingOrders.get(0).symbol());
        assertEquals(buyOrderNotToBeConfirmed.quantity(), pendingOrders.get(0).quantity());
    }

    @Test
    void whenASellerConfirmsASellOrderToBuySomeQuantityFromAnAdvertisementItsAvailableQuantityIsDecreased() {
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
    void aUserCannotPlaceABuyOrderForASellAdvertisementPublishedByHimself() {
        var aUser = registerPepe();

        var aSellAdverticement = publishSellAdverticementFor(aUser);

        assertThrowsDomainExeption(
                "Buyer and seller can't be the same user",
                () -> tradeService.placeBuyOrder(aUser.id(), aSellAdverticement.id(), aSellAdverticement.quantity())
        );
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
