package ar.edu.unq.desapp.grupod.backenddesappapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TradedVolumeTest extends ServiceWithMockedServersTest {

    @Test
    void shouldCalculateVolumeAccordingToCurrentCoinRate() {
        var symbol = CRYPTO_ACTIVE_SYMBOL;
        var assetPriceInDollars = 10.f;
        var dollarToPesoConversionRatio = 202.f;
        var assetPesoPrice = assetPriceInDollars * dollarToPesoConversionRatio;

        mockServerToRespondWithSymbolPrice(symbol, assetPriceInDollars);
        mockServerToRespondWithDollarToPesoRatioOf(dollarToPesoConversionRatio);

        var anInterestedUser = registerPepe();
        var aPublisher = registerJuan();

        var anAdvertisement = publishAdvertisementFor(aPublisher, 20);
        var transactionToConfirm = tradingService.informTransaction(anInterestedUser.id(), anAdvertisement.id(), anAdvertisement.quantity());

        tradingService.confirmTransaction(aPublisher.id(), transactionToConfirm.id());

        var transactions = tradingService.findTransactionsInformedBy(anInterestedUser.id());

        var volume = tradingService.getTradedVolumeBetweenDatesForUser(anInterestedUser.id(), LocalDateTime.parse("2021-12-30T19:34:50.63"), LocalDateTime.now());

        assertEquals(CRYPTO_ACTIVE_SYMBOL, volume.assets().get(0).symbol());
        assertEquals(20, volume.assets().get(0).nominalAmount());
        assertEquals( assetPesoPrice, volume.assets().get(0).currentPriceInPesos());
        assertEquals(assetPriceInDollars, volume.assets().get(0).currentPriceInUsd());
        assertEquals(assetPesoPrice * 20, volume.tradedValueInPesos());
        assertEquals(assetPriceInDollars * 20, volume.tradedValueInUsd());
    }
}
